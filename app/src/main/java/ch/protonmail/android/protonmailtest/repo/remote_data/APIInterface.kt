package ch.protonmail.android.protonmailtest.repo.remote_data

import ch.protonmail.android.protonmailtest.repo.WeatherData
import io.reactivex.Single
import retrofit2.http.GET

interface APIInterface {
    @GET("/api/forecast")
    fun getData(): Single<List<WeatherData>>
}