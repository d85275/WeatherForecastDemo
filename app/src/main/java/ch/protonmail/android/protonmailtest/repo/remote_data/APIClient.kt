package ch.protonmail.android.protonmailtest.repo.remote_data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object APIClient {
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {
        if (retrofit != null) {
            return retrofit
        }
        retrofit = Retrofit.Builder()
            .baseUrl("https://5c5c8ba58d018a0014aa1b24.mockapi.io")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit
    }
}