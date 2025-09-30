package az.legalaid.kinopoisk_clone.di

import az.legalaid.kinopoisk_clone.data.ApiService
import az.legalaid.kinopoisk_clone.data.FilmRepository
import az.legalaid.kinopoisk_clone.data.FilmRepositoryImpl
import az.legalaid.kinopoisk_clone.presentation.FilmsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    //Network
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://s3-eu-west-1.amazonaws.com/sequeniatesttask/")
            .client(get<OkHttpClient>())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }

    //Repository
    single<FilmRepository> {
        FilmRepositoryImpl(
            apiService = get()
        )
    }

    //ViewModel
    viewModel { FilmsViewModel(filmRepository = get()) }
}