package com.remiboulier.rocketboard.mapper

import com.remiboulier.rocketboard.network.dto.LaunchDto
import com.remiboulier.rocketboard.network.dto.LinksDto
import com.remiboulier.rocketboard.network.dto.RocketDetailsDto
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mapstruct.factory.Mappers

/**
 * Created by Remi BOULIER on 30/11/2018.
 * email: boulier.r.job@gmail.com
 */

class LaunchDtoToEntityTest {

    companion object {
        const val FLIGHT_NUMBER = 512
        const val MISSION_NAME = "mission"
        const val UPCOMING = true
        const val LAUNCH_YEAR = 2048
        const val LAUNCH_DATE_UNIX = 1248752012
        const val LAUNCH_DATE_UTC = "12 Dec 2018"
        const val LAUNCH_SUCCESS = true

        const val MISSION_PATCH = "patch big"
        const val MISSION_PATCH_SMALL = "patch small"

        const val ROCKET_ID = "id"
        const val ROCKET_NAME = "name"
    }

    lateinit var launchEntity: LaunchEntity

    @Before
    fun init() {
        val linksDto = LinksDto(
                MISSION_PATCH,
                MISSION_PATCH_SMALL)

        val rocket = RocketDetailsDto(
                ROCKET_ID,
                ROCKET_NAME)

        val launchDto = LaunchDto(
                FLIGHT_NUMBER,
                MISSION_NAME,
                UPCOMING,
                LAUNCH_YEAR,
                LAUNCH_DATE_UNIX,
                LAUNCH_DATE_UTC,
                rocket,
                LAUNCH_SUCCESS,
                linksDto)

        launchEntity = Mappers.getMapper(LaunchDtoToEntity::class.java)
                .map(launchDto)
    }

    @Test
    fun mapped_flightNumber_is_valid() {
        Assert.assertEquals(FLIGHT_NUMBER, launchEntity.flightNumber)
    }

    @Test
    fun mapped_missionName_is_valid() {
        Assert.assertEquals(MISSION_NAME, launchEntity.missionName)
    }

    @Test
    fun mapped_upcoming_is_valid() {
        Assert.assertEquals(UPCOMING, launchEntity.upcoming)
    }

    @Test
    fun mapped_launchYear_is_valid() {
        Assert.assertEquals(LAUNCH_YEAR, launchEntity.launchYear)
    }

    @Test
    fun mapped_launchDateUnix_is_valid() {
        Assert.assertEquals(LAUNCH_DATE_UNIX, launchEntity.launchDateUnix)
    }

    @Test
    fun mapped_launchDateUtc_is_valid() {
        Assert.assertEquals(LAUNCH_DATE_UTC, launchEntity.launchDateUtc)
    }

    @Test
    fun mapped_launchSuccess_is_valid() {
        Assert.assertEquals(LAUNCH_SUCCESS, launchEntity.launchSuccess)
    }

    @Test
    fun mapped_rocketId_is_valid() {
        Assert.assertEquals(ROCKET_ID, launchEntity.rocketId)
    }

    @Test
    fun mapped_rocketName_is_valid() {
        Assert.assertEquals(ROCKET_NAME, launchEntity.rocketName)
    }

    @Test
    fun mapped_missionPatch_is_valid() {
        Assert.assertEquals(MISSION_PATCH, launchEntity.missionPatch)
    }

    @Test
    fun mapped_missionPatchSmall_is_valid() {
        Assert.assertEquals(MISSION_PATCH_SMALL, launchEntity.missionPatchSmall)
    }
}