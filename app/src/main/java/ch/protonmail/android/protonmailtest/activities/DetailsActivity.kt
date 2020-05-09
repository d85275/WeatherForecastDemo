package ch.protonmail.android.protonmailtest.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import ch.protonmail.android.protonmailtest.R
import ch.protonmail.android.protonmailtest.ToastHelper
import ch.protonmail.android.protonmailtest.repo.WeatherData
import ch.protonmail.android.protonmailtest.viewmodels.DetailViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import kotlinx.android.synthetic.main.activity_details.*


/**
 * Created by ProtonMail on 2/25/19.
 * Shows all the details for a particular day.
 */
class DetailsActivity : AppCompatActivity(), DownloadListener {

    // TODO: Please fix any errors and implement the missing parts (including any UI changes)

    private lateinit var data: WeatherData
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        data = intent.getParcelableExtra("Data")
        setContentView(R.layout.activity_details)
        getViewModel()
        initViewModel()
        displayData()
        setListeners()
        AndroidNetworking.initialize(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        viewModel.setTitle(supportActionBar)
        viewModel.setButtonVisibility(download)
    }

    private fun getViewModel() {
        viewModel = this.run { ViewModelProviders.of(this)[DetailViewModel::class.java] }
    }

    private fun initViewModel() {
        viewModel.setData(data)
        viewModel.setToastHelper(ToastHelper(this))
    }

    private fun setListeners() {
        download.setOnClickListener {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            viewModel.downloadImage(connectivityManager, this)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayData() {
        tvHigh.text = "High: ${viewModel.getHighDegree()}"
        tvLow.text = "Low: ${viewModel.getLowDegree()}"
        tvChanceRain.text = "Chance of Raining: ${viewModel.getRainChance()}"
        tvSunrise.text = "Sunrise: ${viewModel.getSunrise()}"
        tvSunset.text = "Sunset: ${viewModel.getSunset()}"
    }

    override fun onDownloadComplete() {
        ToastHelper(this).show("Download Completed")
        viewModel.setButtonVisibility(download)
    }

    override fun onError(anError: ANError?) {
        TODO("Not yet implemented")
    }

}
