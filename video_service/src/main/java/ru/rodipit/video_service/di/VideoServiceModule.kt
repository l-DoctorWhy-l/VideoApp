package ru.rodipit.video_service.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.rodipit.video_service.api.VideoRepository
import ru.rodipit.video_service.data.VideoApi
import ru.rodipit.video_service.data.VideoRepositoryImpl

val videoServiceModule = module {
    single<VideoApi> {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", TOKEN)
                    .build()


                chain.proceed(request)
            }
            .build()
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VideoApi::class.java)
    }

    factory<VideoRepository> {
        VideoRepositoryImpl(
            api = get(),
            dao = get(),
        )
    }

}

private const val BASE_URL = "https://api.vimeo.com/"
private const val TOKEN = "Bearer 92ce709c5d6de8f4afb0a79eeda67963"
