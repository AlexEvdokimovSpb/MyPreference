package gb.myhomework.mypreference.viewmodel

import android.util.Log
import androidx.lifecycle.Observer
import gb.myhomework.mypreference.Constants
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.Repository
import gb.myhomework.mypreference.ui.HistoryViewState

class HistoryViewModel(val repository: Repository = Repository) :
    BaseViewModel<List<Game>?, HistoryViewState>() {
    val TAG = "HW " + HistoryViewModel::class.java.simpleName

    private val repositoryGames = repository.getGames()

    private val gamesObserver = object : Observer<HistoryGameResult> {
        override fun onChanged(t: HistoryGameResult?) {
            if (t == null) return

            when (t) {
                is HistoryGameResult.Success<*> -> {
                    viewStateLiveData.value = HistoryViewState(games = t.data as? List<Game>)
                }
                is HistoryGameResult.Error -> {
                    viewStateLiveData.value = HistoryViewState(error = t.error)
                }
            }
        }
    }

    init {
        viewStateLiveData.value = HistoryViewState()
        repositoryGames.observeForever(gamesObserver)
        if (Constants.DEBUG) {
            Log.v(TAG, "HistoryViewModel init")
        }
    }

    override fun onCleared() {
        repositoryGames.removeObserver(gamesObserver)
    }

}