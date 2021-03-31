package gb.myhomework.mypreference

import androidx.multidex.MultiDexApplication
import gb.myhomework.mypreference.di.*
import org.koin.core.context.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule, splashModule, historyModule, gameHistoryModule)
        }
    }
}
