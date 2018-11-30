package com.remiboulier.rocketboard.network

import com.remiboulier.rocketboard.network.dto.LaunchDto
import com.remiboulier.rocketboard.network.dto.RocketDto
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

interface SpaceXApi {

    @GET("rockets")
    fun getRockets(): Observable<MutableList<RocketDto>>

    @GET("launches")
    fun getLaunches(): Observable<MutableList<LaunchDto>>
}