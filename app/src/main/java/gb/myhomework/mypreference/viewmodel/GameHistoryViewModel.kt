package gb.myhomework.mypreference.viewmodel

import androidx.lifecycle.Observer
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.Repository
import gb.myhomework.mypreference.ui.GameHistoryViewState

class GameHistoryViewModel(val repository: Repository = Repository) :
    BaseViewModel<Game?, GameHistoryViewState>() {

    private var pendingGame: Game? = null

    fun saveChanges(game: Game) {
        pendingGame = game
    }

    override fun onCleared() {
        if (pendingGame != null) {
            repository.saveGame(pendingGame!!)
        }
    }

    fun loadGame(gameId: String) {
        repository.getGameById(gameId).observeForever(object : Observer<HistoryGameResult> {
            override fun onChanged(t: HistoryGameResult?) {
                if (t == null) return

                when (t) {
                    is HistoryGameResult.Success<*> ->
                        viewStateLiveData.value = GameHistoryViewState(game = t.data as? Game)
                    is HistoryGameResult.Error ->
                        viewStateLiveData.value = GameHistoryViewState(error = t.error)
                }
            }
        })
    }
}
