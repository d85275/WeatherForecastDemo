package ch.protonmail.android.protonmailtest.activities

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import ch.protonmail.android.protonmailtest.*
import ch.protonmail.android.protonmailtest.adapters.TabsAdapter
import ch.protonmail.android.protonmailtest.repo.WeatherData
import ch.protonmail.android.protonmailtest.repo.Repository
import ch.protonmail.android.protonmailtest.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener,
    IClickedCallback {
    private lateinit var viewModel: MainViewModel

    companion object {
        private val TAG = "MAIN_ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        if (!isStoragePermissionGranted()) {
            return
        }
        setContentView(R.layout.activity_main)
        setViewModel()
        updateRemoteData()
        setPager()
        initTabs()
        viewModel.setTitle(supportActionBar)

    }

    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted")
                true
            } else {
                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            setContentView(R.layout.activity_main)
            setViewModel()
            updateRemoteData()
            setPager()
            initTabs()
            viewModel.setTitle(supportActionBar)
        } else {
            ToastHelper(this).show("Permission Denied")
            finish()
        }
    }

    private fun setViewModel() {
        viewModel = this.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        }
        viewModel.setRepository(Repository(baseContext))
    }

    private fun updateRemoteData() {
        viewModel.getWeatherData()
    }

    private fun setPager() {
        pager.adapter =
            TabsAdapter(
                this,
                supportFragmentManager
            )
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(this)
    }

    private fun initTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"))
        tabLayout.addTab(tabLayout.newTab().setText("Hottest"))
    }

    override fun onTabReselected(p0: TabLayout.Tab?) {
    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        pager.currentItem = p0!!.position
    }

    override fun onClicked(weatherData: WeatherData) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable("Data", weatherData)
        intent.putExtras(bundle)
        intent.setClass(this, DetailsActivity::class.java)
        startActivity(intent)
    }
}
