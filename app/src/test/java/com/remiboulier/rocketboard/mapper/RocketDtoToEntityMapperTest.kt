package com.remiboulier.rocketboard.mapper

import com.remiboulier.rocketboard.network.dto.EnginesDto
import com.remiboulier.rocketboard.network.dto.RocketDto
import com.remiboulier.rocketboard.room.entity.RocketEntity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mapstruct.factory.Mappers

/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */
class RocketDtoToEntityMapperTest {

    companion object {
        const val ID = 48
        const val ACTIVE = true
        const val COUNTRY = "London"
        const val DESCRIPTION = "Desc"
        const val ROCKET_ID = "id"
        const val ROCKET_NAME = "name"
        const val NUMBER = 1337
    }

    lateinit var rocketEntity: RocketEntity

    @Before
    fun init() {
        val enginesDto = EnginesDto(NUMBER)

        val rocketDto = RocketDto(
                ID,
                ACTIVE,
                COUNTRY,
                DESCRIPTION,
                ROCKET_ID,
                ROCKET_NAME,
                enginesDto)

        rocketEntity = Mappers.getMapper(RocketDtoToEntityMapper::class.java)
                .map(rocketDto)
    }

    @Test
    fun mapped_id() {
        Assert.assertEquals(ID, rocketEntity.id)
    }

    @Test
    fun mapped_active() {
        Assert.assertEquals(ACTIVE, rocketEntity.active)
    }

    @Test
    fun mapped_country() {
        Assert.assertEquals(COUNTRY, rocketEntity.country)
    }

    @Test
    fun mapped_description() {
        Assert.assertEquals(DESCRIPTION, rocketEntity.description)
    }

    @Test
    fun mapped_rocket_id() {
        Assert.assertEquals(ROCKET_ID, rocketEntity.rocketId)
    }

    @Test
    fun mapped_rocket_name() {
        Assert.assertEquals(ROCKET_NAME, rocketEntity.rocketName)
    }

    @Test
    fun mapped_number_of_engine() {
        Assert.assertEquals(NUMBER, rocketEntity.numberOfEngines)
    }
}