package gb.myhomework.mypreference.model.providers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.NoAuthException
import gb.myhomework.mypreference.model.User

private const val GAMES_COLLECTION = "games"
private const val USERS_COLLECTION = "users"

class FireStoreProvider : RemoteDataProvider {

    private var db = FirebaseFirestore.getInstance()
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser


    override fun subscribeToAllGames(): LiveData<HistoryGameResult> =
        MutableLiveData<HistoryGameResult>().apply {
            try {
                getUserNotesCollection().addSnapshotListener { snapshot, error ->
                    value = error?.let { HistoryGameResult.Error(it) }
                        ?: snapshot?.let { query ->
                            val games = query.documents.map { document ->
                                document.toObject(Game::class.java)
                            }
                            HistoryGameResult.Success(games)
                        }
                }
            } catch (e: Throwable) {
                value = HistoryGameResult.Error(e)
            }
        }

    override fun getGameById(id: String): LiveData<HistoryGameResult> =
        MutableLiveData<HistoryGameResult>().apply {
            try {
                getUserNotesCollection().document(id)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        value =
                            HistoryGameResult.Success(snapshot.toObject(Game::class.java))
                    }.addOnFailureListener { exception ->
                        value = HistoryGameResult.Error(exception)
                    }
            } catch (e: Throwable) {
                value = HistoryGameResult.Error(e)
            }
        }

    override fun saveGame(game: Game): LiveData<HistoryGameResult> =
        MutableLiveData<HistoryGameResult>().apply {
            try {
                getUserNotesCollection().document(game.id)
                    .set(game)
                    .addOnSuccessListener {
                        Log.d(TAG, "Game $game is saved")
                        value = HistoryGameResult.Success(game)
                    }.addOnFailureListener {
                        OnFailureListener { exception ->
                            Log.d(TAG, "Error saving game $game, message: ${exception.message}")
                            value = HistoryGameResult.Error(exception)
                        }
                    }
            } catch (e: Throwable) {
                value = HistoryGameResult.Error(e)
            }
        }

    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION)
            .document(it.uid)
            .collection(GAMES_COLLECTION)
    } ?: throw NoAuthException()

    override fun getCurrentUser(): LiveData<User?> =
        MutableLiveData<User?>().apply {
            value = currentUser?.let {
                User(
                    it.displayName ?: "",
                    it.email ?: ""
                )
            }
        }

    companion object {
        private val TAG = "HW ${FireStoreProvider::class.java.simpleName} :"
    }

}