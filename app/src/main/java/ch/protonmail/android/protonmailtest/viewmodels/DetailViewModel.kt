package ch.protonmail.android.protonmailtest.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import ch.protonmail.android.protonmailtest.ToastHelper
import ch.protonmail.android.protonmailtest.repo.WeatherData
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interfaces.DownloadListener
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

class DetailViewModel : ViewModel() {

    private lateinit var data: WeatherData
    private lateinit var toastHelper: ToastHelper

    companion object {
        private const val TAG = "DETAIL_VIEW_MODEL"
    }

    fun setData(data: WeatherData) {
        this.data = data
    }

    fun setToastHelper(toastHelper: ToastHelper) {
        this.toastHelper = toastHelper
    }

    fun getSunrise(): String {
        return getTime(data.sunrise)
    }

    fun getSunset(): String {
        return getTime(data.sunset)
    }

    private fun getTime(totalSecs: Int): String {
        val hours = totalSecs / 3600
        val minutes = (totalSecs % 3600) / 60
        val seconds = totalSecs % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun getRainChance(): String {
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.CEILING
        val num = data.chanceRain * 100
        return "${df.format(num)} %"
    }

    private fun getDegree(degree: Int): String {
        return "$degree °C"
    }

    fun getHighDegree(): String {
        return getDegree(data.high)
    }

    fun getLowDegree(): String {
        return getDegree(data.low)
    }

    fun setTitle(supportActionBar: androidx.appcompat.app.ActionBar?) {
        if (supportActionBar != null) {
            val title = "Day " + data.day + ": " + data.des
            supportActionBar!!.title = title
        } else {
            Log.e(TAG, "action bar is null")
        }
    }

    fun downloadImage(connectivityManager: ConnectivityManager, listener: DownloadListener) {
        if (isNetworkAvailable(connectivityManager)) {
            download(listener)
        } else {
            toastHelper.show("No network connection")
        }
    }

    private fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun download(listener: DownloadListener) {
        try {
            val dirPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/Image"
            val fileName = "Day ${data.day}.jpeg"
            //val file = File(dirPath, fileName)
            AndroidNetworking.download(data.imageUrl, dirPath, fileName).build()
                .startDownload(listener)
        } catch (e: Exception) {
            Log.e(TAG, "error when download image: ${e.toString()}")
        }
    }

    fun setButtonVisibility(view: View) {
        if (isImageExist()) {
            view.visibility = View.INVISIBLE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    private fun isImageExist(): Boolean {
        val dirPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/Image"
        val fileName = "Day ${data.day}.jpeg"
        return File(dirPath, fileName).exists()
    }
}