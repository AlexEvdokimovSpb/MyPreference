package gb.myhomework.mypreference.viewmodel

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import gb.myhomework.mypreference.Constants
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.Repository
import gb.myhomework.mypreference.ui.HistoryViewState

class HistoryViewModel(val repository: Repository) :
    BaseViewModel<List<Game>?, HistoryViewState>() {
    val TAG = "HW " + HistoryViewModel::class.java.simpleName

    private val repositoryGames = repository.getGames()

    private val gamesObserver =
        Observer<HistoryGameResult> { t ->
            if (t == null) return@Observer

            when (t) {
                is HistoryGameResult.Success<*> -> {
                    viewStateLiveData.value = HistoryViewState(games = t.data as? List<Game>)
                }
                is HistoryGameResult.Error -> {
                    viewStateLiveData.value = HistoryViewState(error = t.error)
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

    @VisibleForTesting (otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        repositoryGames.removeObserver(gamesObserver)
    }

}