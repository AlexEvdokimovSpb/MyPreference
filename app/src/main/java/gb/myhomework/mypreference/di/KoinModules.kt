package gb.myhomework.mypreference.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import gb.myhomework.mypreference.model.Repository
import gb.myhomework.mypreference.model.providers.FireStoreProvider
import gb.myhomework.mypreference.model.providers.RemoteDataProvider
import gb.myhomework.mypreference.viewmodel.GameHistoryViewModel
import gb.myhomework.mypreference.viewmodel.HistoryViewModel
import gb.myhomework.mypreference.viewmodel.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val historyModule = module {
    viewModel { HistoryViewModel(get()) }
}

val gameHistoryModule = module {
    viewModel { GameHistoryViewModel(get()) }
}

