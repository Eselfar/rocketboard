package com.remiboulier.rocketboard.network

import com.remiboulier.rocketboard.model.Launch
import com.remiboulier.rocketboard.model.Rocket
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

interface SpaceXApi {

    @GET("rockets")
    fun getRockets(): Observable<MutableList<Rocket>>

    @GET("launches")
    fun getLaunches(): Observable<MutableList<Launch>>
}