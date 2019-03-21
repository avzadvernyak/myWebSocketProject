package kampukter.myWebSocketProject

import kampukter.myWebSocketProject.Repository.InfoSensorRepository
import kampukter.myWebSocketProject.Repository.WebSocketRepository
import kampukter.myWebSocketProject.ViewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val APPLICATION_MODULE = module {
    single { WebSocketRepository() }
    single { InfoSensorRepository() }
    viewModel { MainViewModel(get(), get()) }
}