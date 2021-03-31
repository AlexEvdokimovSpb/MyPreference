package gb.myhomework.mypreference.ui

import gb.myhomework.mypreference.model.Game

class HistoryViewState(val games: List<Game>? = null, error: Throwable? = null) :
    BaseViewState<List<Game>?>(games, error)