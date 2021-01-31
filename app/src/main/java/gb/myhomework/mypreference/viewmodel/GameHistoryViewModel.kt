package gb.myhomework.mypreference.viewmodel

import androidx.lifecycle.ViewModel
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.Repository

class GameHistoryViewModel(private val repository: Repository = Repository) : ViewModel() {

    private var pendingGame: Game? = null

    fun saveChanges(game: Game) {
        pendingGame = game
    }

    override fun onCleared() {
        if (pendingGame != null) {
            repository.saveGame(pendingGame!!)
        }
    }
}