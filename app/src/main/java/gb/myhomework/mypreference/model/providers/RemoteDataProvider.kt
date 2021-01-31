package gb.myhomework.mypreference.model.providers

import androidx.lifecycle.LiveData
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.User

interface RemoteDataProvider {
    fun subscribeToAllGames(): LiveData<HistoryGameResult>
    fun getGameById(id: String): LiveData<HistoryGameResult>
    fun saveGame(game: Game): LiveData<HistoryGameResult>
    fun getCurrentUser(): LiveData<User?>
}