package com.remiboulier.rocketboard.model

/**
 * Created by Remi BOULIER on 27/11/2018.
 * email: boulier.r.job@gmail.com
 */

import com.google.gson.annotations.SerializedName

data class Rocket(
        @SerializedName("id") val id: Int,
        @SerializedName("active") val active: Boolean,
        @SerializedName("country") val country: String,
        @SerializedName("description") val description: String,
        @SerializedName("rocket_id") val rocketId: String,
        @SerializedName("rocket_name") val rocketName: String,
        @SerializedName("engines") val engines: Engines)

data class Engines(@SerializedName("number") val number: Int)