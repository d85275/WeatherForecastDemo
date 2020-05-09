package ch.protonmail.android.protonmailtest.repo.local_data

import androidx.room.Database
import androidx.room.RoomDatabase
import ch.protonmail.android.protonmailtest.repo.WeatherData

@Database(entities = [WeatherData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDataDao(): WeatherDao
}