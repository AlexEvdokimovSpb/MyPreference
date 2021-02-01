package gb.myhomework.mypreference.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.*

private const val GAMES_COLLECTION = "games"

class FireStoreProvider : RemoteDataProvider {

    private var db = FirebaseFirestore.getInstance()
    private val gamesReference = db.collection(GAMES_COLLECTION)

    override fun subscribeToAllGames(): LiveData<HistoryGameResult> {
        val result = MutableLiveData<HistoryGameResult>()

        gamesReference.addSnapshotListener { value, error ->
            if (error != null) {
                result.value = HistoryGameResult.Error(error)
            } else if (value != null) {
                val games = mutableListOf<Game>()

                for (doc: QueryDocumentSnapshot in value) {
                    games.add(doc.toObject(Game::class.java))
                }
                result.value = HistoryGameResult.Success(games)
            }
        }
        return result
    }

    override fun getGameById(id: String): LiveData<HistoryGameResult> {
        val result = MutableLiveData<HistoryGameResult>()

        gamesReference.document(id)
            .get()
            .addOnSuccessListener { snapshot ->
                result.value =
                    HistoryGameResult.Success(snapshot.toObject(Game::class.java))
            }.addOnFailureListener { exception ->
                result.value = HistoryGameResult.Error(exception)
            }
        return result
    }

    override fun saveGame(game: Game): LiveData<HistoryGameResult> {
        val result = MutableLiveData<HistoryGameResult>()

        gamesReference.document(game.id)
            .set(game)
            .addOnSuccessListener {
                Log.d(TAG, "Game $game is saved")
                result.value = HistoryGameResult.Success(game)
            }.addOnFailureListener {
                OnFailureListener { exception ->
                    Log.d(TAG, "Error saving game $game, message: ${exception.message}")
                    result.value = HistoryGameResult.Error(exception)
                }
            }
        return result
    }

    companion object {
        private val TAG = "HW ${FireStoreProvider::class.java.simpleName} :"
    }
}