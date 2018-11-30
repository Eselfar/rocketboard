package com.remiboulier.rocketboard.room.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Remi BOULIER on 29/11/2018.
 * email: boulier.r.job@gmail.com
 */

@Entity(tableName = "launch")
data class LaunchEntity(
        @PrimaryKey(autoGenerate = false) var flightId: Int,
        @ColumnInfo(name = "flight_number") var flightNumber: Int,
        @ColumnInfo(name = "mission_name") var missionName: String? = null,
        @ColumnInfo(name = "upcoming") var upcoming: Boolean,
        @ColumnInfo(name = "launch_year") var launchYear: Int,
        @ColumnInfo(name = "launch_date_unix") var launchDateUnix: Int? = null,
        @ColumnInfo(name = "launch_date_utc") var launchDateUtc: String? = null,
        @ColumnInfo(name = "launch_success") var launchSuccess: Boolean? = null,

        @ColumnInfo(name = "rocket_id") var rocketId: String,
        @ColumnInfo(name = "rocket_name") var rocketName: String,

        @ColumnInfo(name = "mission_patch") var missionPatch: String? = null,
        @ColumnInfo(name = "mission_patch_small") var missionPatchSmall: String? = null) {

    // Necessary for MapStruct
    @Ignore
    constructor() : this(
            0,
            0,
            null,
            false,
            0,
            null,
            null,
            null,
            "",
            "",
            null,
            null)
}