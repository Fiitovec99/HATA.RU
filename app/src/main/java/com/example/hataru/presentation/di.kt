package com.example.hataru.presentation

import com.example.hataru.data.FlatsImpl
import com.example.hataru.data.PhotosImpl
import com.example.hataru.data.PhotosService
import com.example.hataru.domain.FlatsRep
import com.example.hataru.domain.GetFlatsUseCase
import com.example.hataru.domain.GetPhotosUseCase
import com.example.hataru.domain.PhotosRep
import com.example.hataru.domain.ApiService
import com.example.hataru.domain.entity.MyCookieJar
import com.example.hataru.presentation.viewModels.FlatBottomSheetViewModel
import com.example.hataru.presentation.viewModels.FlatViewModel
import com.example.hataru.presentation.viewModels.ListFlatsViewModel
import com.example.hataru.presentation.viewModels.MapViewModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


var appModule = module {

    factory<GetFlatsUseCase> { GetFlatsUseCase(rep = get()) }
    factory<GetPhotosUseCase> { GetPhotosUseCase(rep = get()) }

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

    single<PhotosService> {
        Retrofit.Builder()
            .baseUrl("https://public-api.reservationsteps.ru/v1/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(PhotosService::class.java)
    }


    single<FlatsRep> { FlatsImpl(ser = get()) }
    single<PhotosRep> { PhotosImpl(ser = get())}

}

val appMod = module {
    viewModel<MapViewModel> { MapViewModel(useCase = get(), photosCase = get()) }
    viewModel<ListFlatsViewModel> { ListFlatsViewModel(rep = get(),photos = get()) }
    viewModel<FlatViewModel> { FlatViewModel(photos = get()) }
    viewModel<FlatBottomSheetViewModel> { FlatBottomSheetViewModel(photosCase = get()) }
}


