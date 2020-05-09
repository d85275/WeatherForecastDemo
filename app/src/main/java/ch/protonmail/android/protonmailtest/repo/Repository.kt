package ch.protonmail.android.protonmailtest.repo

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ch.protonmail.android.protonmailtest.repo.local_data.AppDatabase
import ch.protonmail.android.protonmailtest.repo.remote_data.APIClient.getClient
import ch.protonmail.android.protonmailtest.repo.remote_data.APIInterface
import io.reactivex.Single


class Repository(private val ctx: Context) {

    fun getRemoteData(): Single<List<WeatherData>> {
        val apiInterface = getClient()!!.create(APIInterface::class.java)
        return apiInterface.getData()
    }

    private val db = Room.databaseBuilder(ctx, AppDatabase::class.java, "WeatherDataDb").build()

    fun getLocalData(): Single<List<WeatherData>> {
        return db.weatherDataDao().getAll()
    }

    fun saveLocalData(data: List<WeatherData>) {
        db.weatherDataDao().upsert(data)
    }
}