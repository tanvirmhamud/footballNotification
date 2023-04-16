package com.example.backgroundservice.Retrofit_heloer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofithelper {

    val baseUrl = "http://gtvcricketlive.com/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}