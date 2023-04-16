package com.example.backgroundservice.Api_Interface.LiveMatch

import com.example.backgroundservice.Model.Live.Livematch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface Livematchinterface {
    @Headers("ab: live-soccer-tv-footballl-live-tv")
    @GET("fixtures/live")
    suspend fun getQuotes() : Response<Livematch>
}