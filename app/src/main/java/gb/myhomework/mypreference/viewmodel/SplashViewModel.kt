package gb.myhomework.mypreference.viewmodel

import gb.myhomework.mypreference.model.NoAuthException
import gb.myhomework.mypreference.model.Repository
import gb.myhomework.mypreference.ui.SplashViewState

class SplashViewModel(private val repository: Repository = Repository) :
    BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        repository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(isAuth = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}
