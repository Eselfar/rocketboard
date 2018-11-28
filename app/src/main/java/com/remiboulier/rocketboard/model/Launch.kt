package com.remiboulier.rocketboard.model

/**
 * Created by Remi BOULIER on 28/11/2018.
 * email: boulier.r.job@gmail.com
 */

import com.google.gson.annotations.SerializedName

data class Launch(
        @SerializedName("flight_number") val flightNumber: Int,
        @SerializedName("mission_name") val missionName: String? = null,
        @SerializedName("upcoming") val upcoming: Boolean,
        @SerializedName("launch_year") val launchYear: Int? = null,
        @SerializedName("launch_date_unix") val launchDateUnix: Long? = null,
        @SerializedName("launch_date_utc") val launchDateUtc: String? = null,
        @SerializedName("rocket") val rocket: RocketDetails,
        @SerializedName("launch_success") val launchSuccess: Boolean? = null,
        @SerializedName("links") val links: Links
)

data class Links(
        @SerializedName("mission_patch") val missionPatch: String? = null,
        @SerializedName("mission_patch_small") val missionPatchSmall: String? = null
)

data class RocketDetails(
        @SerializedName("rocket_id") val rocketId: String,
        @SerializedName("rocket_name") val rocketName: String
)
