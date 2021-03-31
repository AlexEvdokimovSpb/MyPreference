package gb.myhomework.mypreference.viewmodel

import gb.myhomework.mypreference.model.NoAuthException
import gb.myhomework.mypreference.model.Repository
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: Repository) :
    BaseViewModel<Boolean>() {

    fun requestUser() {
        launch {
            repository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }
}
