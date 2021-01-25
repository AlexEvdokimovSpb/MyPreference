package gb.myhomework.mypreference.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gb.myhomework.mypreference.Constants
import gb.myhomework.mypreference.model.Repository
import gb.myhomework.mypreference.ui.HistoryViewState

class HistoryViewModel  : ViewModel() {
    val TAG = "HW " + HistoryViewModel::class.java.simpleName
    private val viewStateLiveData: MutableLiveData<HistoryViewState> = MutableLiveData()

    init {
        viewStateLiveData.value = HistoryViewState(Repository.getGames())
        if (Constants.DEBUG) {
            Log.v(TAG, "HistoryViewModel init" )
        }
    }

    fun viewState(): LiveData<HistoryViewState> = viewStateLiveData
}