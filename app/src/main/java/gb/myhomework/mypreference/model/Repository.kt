package gb.myhomework.mypreference.model

import gb.myhomework.mypreference.model.providers.RemoteDataProvider

class Repository (private val remoteDataProvider: RemoteDataProvider){

    suspend fun getGames() = remoteDataProvider.subscribeToAllGames()
    suspend fun saveGame(game: Game) = remoteDataProvider.saveGame(game)
    suspend fun getGameById(id: String) = remoteDataProvider.getGameById(id)
    suspend fun getCurrentUser() = remoteDataProvider.getCurrentUser()
    suspend fun deleteGame(gameId: String) = remoteDataProvider.deleteGame(gameId)
}