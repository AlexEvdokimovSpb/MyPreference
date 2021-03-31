package gb.myhomework.mypreference.model.providers

import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.User
import kotlinx.coroutines.channels.ReceiveChannel

interface RemoteDataProvider {
    suspend fun subscribeToAllGames(): ReceiveChannel<HistoryGameResult>
    suspend fun getGameById(id: String): Game
    suspend fun saveGame(game: Game): Game
    suspend fun getCurrentUser(): User?
    suspend fun deleteGame(gameId: String): Game?
}