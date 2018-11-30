package com.remiboulier.rocketboard.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.remiboulier.rocketboard.room.entity.LaunchEntity

/**
 * Created by Remi BOULIER on 29/11/2018.
 * email: boulier.r.job@gmail.com
 */

@Dao
interface LaunchDao {

    @Query("SELECT * FROM launch")
    fun getAll(): MutableList<LaunchEntity>

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg launches: LaunchEntity)
}