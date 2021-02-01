package gb.myhomework.mypreference.model

object Repository{
    private val remoteDataProvider: RemoteDataProvider = FireStoreProvider()

    fun getGames() = remoteDataProvider.subscribeToAllGames()
    fun saveGame(game: Game) = remoteDataProvider.saveGame(game)
    fun getGameById(id: String) = remoteDataProvider.getGameById(id)

}