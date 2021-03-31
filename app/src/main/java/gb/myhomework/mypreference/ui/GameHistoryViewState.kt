package gb.myhomework.mypreference.ui

import gb.myhomework.mypreference.model.Game

class GameHistoryViewState(
    data: Data = Data(),
    error: Throwable? = null
) :
    BaseViewState<GameHistoryViewState.Data>(data, error) {
    data class Data(val isDeleted: Boolean = false, val game: Game? = null)
}
