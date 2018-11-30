package com.remiboulier.rocketboard.mapper

import com.remiboulier.rocketboard.network.dto.LaunchDto
import com.remiboulier.rocketboard.room.entity.LaunchEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Created by Remi BOULIER on 29/11/2018.
 * email: boulier.r.job@gmail.com
 */

@Mapper
interface LaunchDtoToEntity {

    @Mappings(
            Mapping(source = "rocket.rocketId", target = "rocketId"),
            Mapping(source = "rocket.rocketName", target = "rocketName"),
            Mapping(source = "links.missionPatch", target = "missionPatch"),
            Mapping(source = "links.missionPatchSmall", target = "missionPatchSmall")
    )
    fun map(dto: LaunchDto): LaunchEntity
}