package gb.myhomework.mypreference.viewmodel

import androidx.annotation.VisibleForTesting
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.Repository
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HistoryViewModel(val repository: Repository) :
    BaseViewModel<List<Game>?>() {

    private val gamesChannel by lazy { runBlocking { repository.getGames() } }

    init {
        launch {
            gamesChannel.consumeEach { result ->
                when (result) {
                    is HistoryGameResult.Success<*> -> setData(result.data as? List<Game>)
                    is HistoryGameResult.Error -> setError(result.error)
                }
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        gamesChannel.cancel()
        super.onCleared()
    }
}