package com.marwaeltayeb.currencyexchange.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.marwaeltayeb.currencyexchange.data.model.RateApiResponse
import com.marwaeltayeb.currencyexchange.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatesRepository {

    private val latestRatesLiveData: MutableLiveData<List<Pair<String,Double>>> = MutableLiveData<List<Pair<String,Double>>>()
    private val exchangeRateLiveData: MutableLiveData<List<Pair<String,Double>>> = MutableLiveData<List<Pair<String,Double>>>()

    fun requestLatestRatesLiveData(base: String) {
        RetrofitClient.getRateService().getLatestRatesByBase(base)
            .enqueue(object : Callback<RateApiResponse> {
                override fun onFailure(call: Call<RateApiResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

                override fun onResponse(call: Call<RateApiResponse>, response: Response<RateApiResponse>) {
                    if (response.isSuccessful) {
                        Log.v("onResponse", "Succeeded")

                        if (response.body() != null) {
                            val ratesList: List<Pair<String,Double>> = response.body()!!.rates.toList()
                            latestRatesLiveData.setValue(ratesList)
                        }
                    }
                }
            })
    }

    fun getLatestRatesLiveData(): MutableLiveData<List<Pair<String,Double>>> {
        return latestRatesLiveData
    }

    fun requestExchangeRateLiveData(base: String, symbol: String) {
        RetrofitClient.getRateService().getSpecificExchangeRate(base, symbol)
            .enqueue(object : Callback<RateApiResponse> {
                override fun onFailure(call: Call<RateApiResponse>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

                override fun onResponse(call: Call<RateApiResponse>, response: Response<RateApiResponse>) {
                    if (response.isSuccessful) {
                        Log.v("onResponse", "Succeeded")

                        if (response.body() != null) {
                            exchangeRateLiveData.setValue(response.body()!!.rates.toList())
                        }
                    }
                }
            })
    }

    fun getExchangeRateLiveData(): MutableLiveData<List<Pair<String,Double>>>{
        return exchangeRateLiveData
    }
}