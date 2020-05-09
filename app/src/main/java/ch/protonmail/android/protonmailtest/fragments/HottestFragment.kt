package ch.protonmail.android.protonmailtest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import ch.protonmail.android.protonmailtest.IClickedCallback
import ch.protonmail.android.protonmailtest.viewmodels.MainViewModel
import ch.protonmail.android.protonmailtest.adapters.ForecastAdapter
import ch.protonmail.android.protonmailtest.R
import kotlinx.android.synthetic.main.fragment_upcoming.*
import java.lang.Exception

/**
 * Created by ProtonMail on 2/25/19.
 * Shows any days that have less than a 50% chance of rain, ordered hottest to coldest
 * */
class HottestFragment(val callback: IClickedCallback) : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ForecastAdapter

    // TODO: Please fix any errors and implement the missing parts (including any UI changes)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hottest, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModel()
        setAdapter()
        registerLiveData()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private fun setAdapter() {

        val layoutManager = LinearLayoutManager(context)
        adapter =
            ForecastAdapter(context!!, viewModel, callback)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = layoutManager
    }

    private fun getViewModel() {
        viewModel = activity?.run { ViewModelProviders.of(this)[MainViewModel::class.java] }
            ?: throw Exception("Invalid Activity")
    }

    private fun registerLiveData() {
        viewModel.getWeatherLiveData().observe(this, Observer {
            adapter.setData(viewModel.getHottestData())
            adapter.notifyDataSetChanged()
        })
    }
}