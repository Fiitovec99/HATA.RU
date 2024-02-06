package com.example.hataru.presentation

import com.example.hataru.data.FlatsImpl
import com.example.hataru.domain.FlatsRep
import com.example.hataru.domain.GetFlatsUseCase
import com.example.hataru.migration.ApiService
import com.example.hataru.migration.MyCookieJar
import com.example.hataru.presentation.viewModels.MapViewModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


var appModule = module {

    factory<GetFlatsUseCase> { GetFlatsUseCase(rep = get()) }

    single<ApiService> {
        Retrofit.Builder()
            .baseUrl("https://online.bnovo.ru")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(OkHttpClient.Builder()
                .cookieJar(MyCookieJar())
                .build())
            .build()
            .create(ApiService::class.java)
    }

    single<FlatsRep> { FlatsImpl(ser = get()) }

}

val appMod = module {
    viewModel<MapViewModel> { MapViewModel(useCase = get()) }
}


