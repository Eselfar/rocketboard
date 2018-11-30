package com.remiboulier.rocketboard.mapper

import com.remiboulier.rocketboard.network.dto.RocketDto
import com.remiboulier.rocketboard.room.entity.RocketEntity

import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * Created by Remi BOULIER on 29/11/2018.
 * email: boulier.r.job@gmail.com
 */

@Mapper
interface RocketDtoToEntityMapper {

    @Mapping(target = "numberOfEngines", source = "engines.number")
    fun map(dto: RocketDto): RocketEntity
}
