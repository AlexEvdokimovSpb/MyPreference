package gb.myhomework.mypreference.model.providers

import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.NoAuthException
import gb.myhomework.mypreference.model.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val GAMES_COLLECTION = "games"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : RemoteDataProvider {

    private val currentUser
        get() = firebaseAuth.currentUser

    override suspend fun subscribeToAllGames(): ReceiveChannel<HistoryGameResult> =
        Channel<HistoryGameResult>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null
            try {
                registration =
                    getUserGamesCollection().addSnapshotListener { snapshot, error ->
                        val value = error?.let {
                            HistoryGameResult.Error(it)
                        } ?: snapshot?.let { query ->
                            val games = query.documents.map { document ->
                                document.toObject(Game::class.java)
                            }
                            HistoryGameResult.Success(games)
                        }
                        value?.let { offer(it) }
                    }
            } catch (e: Throwable) {
                offer(HistoryGameResult.Error(e))
            }
            invokeOnClose { registration?.remove() }
        }

    override suspend fun getGameById(id: String): Game =
        suspendCoroutine { continuation ->
            try {
                getUserGamesCollection().document(id).get()
                    .addOnSuccessListener { snapshot ->
                        continuation.resume(snapshot.toObject(Game::class.java)!!)
                    }.addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }

    override suspend fun saveGame(game: Game): Game =
        suspendCoroutine { continuation ->
            try {
                getUserGamesCollection().document(game.id).set(game)
                    .addOnSuccessListener {
                        continuation.resume(game)
                    }.addOnFailureListener {
                        OnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                    }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }

    private fun getUserGamesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION)
            .document(it.uid)
            .collection(GAMES_COLLECTION)
    } ?: throw NoAuthException()

    override suspend fun getCurrentUser(): User? =
        suspendCoroutine { continuation ->
            currentUser?.let {
                continuation.resume(
                    User(
                        it.displayName ?: "",
                        it.email ?: ""
                    )
                )
            } ?: continuation.resume(null)
        }

    companion object {
        private val TAG = "HW ${FireStoreProvider::class.java.simpleName} :"
    }

    override suspend fun deleteGame(gameId: String): Game? =
        suspendCoroutine { continuation ->
            try {
                getUserGamesCollection().document(gameId).delete()
                    .addOnSuccessListener {
                        continuation.resume(null)
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }
}