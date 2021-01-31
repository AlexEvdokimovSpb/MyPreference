package gb.myhomework.mypreference.ui

import android.os.*
import androidx.lifecycle.ViewModelProvider
import gb.myhomework.mypreference.databinding.ActivitySplashBinding
import gb.myhomework.mypreference.viewmodel.SplashViewModel

private const val START_DELAY = 1000L

class SplashActivity() : BaseActivity<Boolean?, SplashViewState>() {

    val TAG = "HW " + SplashActivity::class.java.simpleName

    override val viewModel: SplashViewModel by lazy {
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override val ui: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }
}