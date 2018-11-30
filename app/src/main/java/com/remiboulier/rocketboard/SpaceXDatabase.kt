package com.remiboulier.rocketboard

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.remiboulier.rocketboard.room.dao.LaunchDao
import com.remiboulier.rocketboard.room.dao.RocketDao
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import com.remiboulier.rocketboard.room.entity.RocketEntity

/**
 * Created by Remi BOULIER on 29/11/2018.
 * email: boulier.r.job@gmail.com
 */

@Database(entities = [RocketEntity::class, LaunchEntity::class], version = 1)
abstract class SpaceXDatabase : RoomDatabase() {
    abstract fun rocketDao(): RocketDao
    abstract fun launchDao(): LaunchDao
}


