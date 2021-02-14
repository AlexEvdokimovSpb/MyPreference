package gb.myhomework.mypreference.viewmodel

import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.Repository
import gb.myhomework.mypreference.ui.GameHistoryViewState

class GameHistoryViewModel(val repository: Repository) :
    BaseViewModel<GameHistoryViewState.Data, GameHistoryViewState>() {

    private val currentGame: Game?
        get() = viewStateLiveData.value?.data?.game

    fun saveChanges(game: Game) {
        viewStateLiveData.value = GameHistoryViewState((GameHistoryViewState.Data(game = game)))
    }

    override fun onCleared() {
        currentGame?.let { repository.saveGame(it) }
    }

    fun loadGame(gameId: String) {
        repository.getGameById(gameId).observeForever { result ->
            result?.let { gameResult ->
                viewStateLiveData.value = when (gameResult) {
                    is HistoryGameResult.Success<*> ->
                        GameHistoryViewState(GameHistoryViewState.Data(game = gameResult.data as? Game))
                    is HistoryGameResult.Error ->
                        GameHistoryViewState(error = gameResult.error)
                }
            }
        }
    }

    fun deleteGame() {
        currentGame?.let {
            repository.deleteGame(it.id).observeForever { result ->
                result?.let { gameResult ->
                    viewStateLiveData.value = when (gameResult) {
                        is HistoryGameResult.Success<*> -> GameHistoryViewState(
                            GameHistoryViewState.Data(isDeleted = true))
                        is HistoryGameResult.Error -> GameHistoryViewState(error = gameResult.error)
                    }
                }
            }
        }
    }

}

