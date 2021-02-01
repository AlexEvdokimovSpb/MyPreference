package gb.myhomework.mypreference.model

sealed class HistoryGameResult {

    data class Success<out T>(val data: T) : HistoryGameResult()

    data class Error(val error: Throwable) : HistoryGameResult()

}
