package kampukter.myWebSocketProject

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val APPLICATION_MODULE = module {
    single { WebSocketRepository() }
    viewModel { MainViewModel(get()) }
}