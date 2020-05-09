package ch.protonmail.android.protonmailtest.repo.local_data

import android.database.sqlite.SQLiteConstraintException
import androidx.room.*
import ch.protonmail.android.protonmailtest.repo.WeatherData
import io.reactivex.Single


@Dao
interface WeatherDao {

    @Query("select * from weatherdata")
    fun getAll(): Single<List<WeatherData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data: List<WeatherData>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(data: List<WeatherData>)

    @Transaction
    fun upsert(data: List<WeatherData>) {
        try {
            insert(data)
        }catch (exception: SQLiteConstraintException) {
            update(data)
        }
    }
}