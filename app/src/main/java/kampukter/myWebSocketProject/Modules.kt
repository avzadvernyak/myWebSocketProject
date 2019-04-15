package kampukter.myWebSocketProject

import kampukter.myWebSocketProject.repository.InfoSensorRepository
import kampukter.myWebSocketProject.repository.LocationRepository
import kampukter.myWebSocketProject.repository.WebSocketRepository
import kampukter.myWebSocketProject.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val APPLICATION_MODULE = module {
    single { WebSocketRepository() }
    single { InfoSensorRepository() }
    single { LocationRepository() }
    viewModel { MainViewModel(get(), get(), get()) }
}