package com.remiboulier.rocketboard.room.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Remi BOULIER on 29/11/2018.
 * email: boulier.r.job@gmail.com
 */
@Entity(tableName = "rocket")
data class RocketEntity(
        @PrimaryKey(autoGenerate = false) var id: Int,
        @ColumnInfo(name = "active") var active: Boolean,
        @ColumnInfo(name = "country") var country: String,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "rocket_id") var rocketId: String,
        @ColumnInfo(name = "rocket_name") var rocketName: String,
        @ColumnInfo(name = "number_of_engine") var numberOfEngines: Int) {

    // Necessary for MapStruct
    constructor() : this(
            0,
            false,
            "",
            "",
            "",
            "",
            0)
}
