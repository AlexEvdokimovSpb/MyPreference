package gb.myhomework.mypreference.model

import androidx.lifecycle.LiveData

interface RemoteDataProvider {
    fun subscribeToAllGames(): LiveData<HistoryGameResult>
    fun getGameById(id: String): LiveData<HistoryGameResult>
    fun saveGame(game: Game): LiveData<HistoryGameResult>
}