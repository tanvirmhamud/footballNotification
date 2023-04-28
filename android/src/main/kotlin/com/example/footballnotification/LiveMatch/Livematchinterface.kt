package com.example.backgroundservice.Api_Interface.LiveMatch

import com.example.backgroundservice.Model.Live.Livematch
import com.example.footballnotification.SingleMatch.SingleMatchDetails
import retrofit2.Response
import retrofit2.http.*

interface Livematchinterface {

//    @GET("fixtures/live")
//    suspend fun getQuotes(@Header("ab") token: String) : Response<Livematch>

    @GET("fixtures/{id}")
    suspend fun getQuotes(@Path("id") id: String ,@Header("ab") token: String) : Response<Livematch>

    @GET("teamfixtures/team={id}/season={season}")
    suspend fun getteamfixture(@Path("id") id: String,@Path("season") season: String ,@Header("ab") token: String) : Response<Livematch>


}