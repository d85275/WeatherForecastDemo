package ch.protonmail.android.protonmailtest

import ch.protonmail.android.protonmailtest.repo.WeatherData

interface IClickedCallback {
    fun onClicked(weatherData: WeatherData)
}