package com.remiboulier.rocketboard.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.remiboulier.rocketboard.room.entity.RocketEntity
import io.reactivex.Single

/**
 * Created by Remi BOULIER on 29/11/2018.
 * email: boulier.r.job@gmail.com
 */

@Dao
interface RocketDao {

    @Insert(onConflict = REPLACE)
    fun saveAll(rockets: List<RocketEntity>)

    @Query("SELECT * FROM rocket")
    fun getAllAsync(): Single<List<RocketEntity>>
}