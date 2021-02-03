package gb.myhomework.mypreference.model

import gb.myhomework.mypreference.model.providers.FireStoreProvider
import gb.myhomework.mypreference.model.providers.RemoteDataProvider

object Repository {
    private val remoteDataProvider: RemoteDataProvider = FireStoreProvider()

    fun getGames() = remoteDataProvider.subscribeToAllGames()
    fun saveGame(game: Game) = remoteDataProvider.saveGame(game)
    fun getGameById(id: String) = remoteDataProvider.getGameById(id)
    fun getCurrentUser() = remoteDataProvider.getCurrentUser()
    fun deleteGame(gameId: String) = remoteDataProvider.deleteGame(gameId)
}