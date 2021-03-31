package gb.myhomework.mypreference.viewmodel

import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.Repository
import gb.myhomework.mypreference.ui.GameHistoryViewState
import kotlinx.coroutines.launch

class GameHistoryViewModel(val repository: Repository) :
    BaseViewModel<GameHistoryViewState.Data>() {

    private val currentGame: Game?
        get() = getViewState().poll()?.game

    fun saveChanges(game: Game) {
        setData(GameHistoryViewState.Data(game = game))
    }

    fun loadGame(gameId: String) {
        launch {
            try {
                setData(GameHistoryViewState.Data(game = repository.getGameById(gameId)))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun deleteGame() {
        launch {
            try {
                currentGame?.let { repository.deleteGame(it.id) }
                setData(GameHistoryViewState.Data(isDeleted = true))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    override fun onCleared() {
        launch {
            currentGame?.let { repository.saveGame(it) }
            super.onCleared()
        }
    }
}

