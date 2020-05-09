package ch.protonmail.android.protonmailtest.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.protonmail.android.protonmailtest.repo.Repository
import ch.protonmail.android.protonmailtest.repo.WeatherData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File


class MainViewModel : ViewModel() {
    private lateinit var repository: Repository
    private val _weatherData = MutableLiveData<List<WeatherData>>()

    companion object {
        private const val TAG = "MAIN_VIEW_MODEL"
    }


    fun setRepository(repo: Repository) {
        repository = repo
    }

    fun setTitle(supportActionBar: androidx.appcompat.app.ActionBar?) {
        if (supportActionBar != null) {
            supportActionBar!!.title = "Forecast"
        } else {
            Log.e(TAG, "action bar is null")
        }
    }

    fun getWeatherData() {
        val disposable = CompositeDisposable()
        val subscribe: Disposable = repository.getRemoteData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                saveLocalData(list)
                _weatherData.postValue(list)
            }, { t: Throwable? ->
                Log.e("VM", "error, ${t!!.printStackTrace()}")
                getLocalData()
            })

        disposable.add(subscribe)
    }

    private fun saveLocalData(list: List<WeatherData>) {
        Single.fromCallable { repository.saveLocalData(list) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun getLocalData() {
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.getLocalData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { e -> Log.e("ViewModel", "error when reading db, $e") }
                .subscribe { list -> _weatherData.postValue(list) }
        )
    }

    fun getThumbImage(day: String): Bitmap? {
        val THUMBSIZE = 128
        val imagePath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/Image/Day ${day}.jpeg"
        val file = File(imagePath)
        if (!file.exists()) {
            return null
        }
        return ThumbnailUtils.extractThumbnail(
            BitmapFactory.decodeFile(imagePath), THUMBSIZE, THUMBSIZE
        )
    }

    fun getWeatherLiveData(): LiveData<List<WeatherData>> {
        return _weatherData
    }

    fun getHottestData(): List<WeatherData> {
        val list = _weatherData.value!!
        val hottest: List<WeatherData> = arrayListOf()
        for (i in list.indices) {
            if (list[i].chanceRain < 0.5) {
                (hottest as ArrayList).add(list[i])
            }
        }

        return hottest.sortedByDescending { it.high }
    }

    fun getUpcomingData(): List<WeatherData> {
        return _weatherData.value!!
    }
}