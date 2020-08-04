package moe.yue.launchlib.database


import moe.yue.launchlib.launchlib.api.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object LaunchLibLaunches : Table("launches") {
    val uuid = varchar("uuid", 36).uniqueIndex()
    val launchLibId = integer("launch_library_id").nullable()
    val slug = varchar("slug", 2048)
    val name = varchar("name", 2048)
    val statusId = integer("status_id")
    val statusDescription = varchar("status", 255)
    val netTime = varchar("net", 20).nullable()
    val windowEndTime = varchar("window_end", 20).nullable()
    val windowStartTime = varchar("window_start", 20).nullable()
    val inHold = bool("inhold").nullable()
    val timeTBD = bool("timetbd").nullable()
    val dateTBD = bool("datetbd").nullable()
    val probability = integer("probability").nullable()
    val holdReason = varchar("hold_reason", 2048).nullable()
    val failReason = varchar("fail_reason", 2048).nullable()
    val hashtag = varchar("hashtag", 2048).nullable()
    val infoUrls = text("info_urls").nullable()
    val videoUrls = text("video_urls").nullable()
    val hasWebcast = bool("has_webcast")
    val imageUrl = text("image_url").nullable()
    val infographicUrl = text("infographic_url").nullable()
    val agencyId = integer("agency_id")
    val agencyName = varchar("agency", 200)
    val agencyType = varchar("agency_type", 255).nullable()
    val rocketId = integer("rocket_id")
    val rocketIdAlt = integer("rocket_id_alt").nullable()
    val rocketLaunchLibId = integer("rocket_launch_library_id").nullable()
    val rocketName = varchar("rocket_name", 200).nullable()
    val rocketFamily = varchar("rocket_family", 200).nullable()
    val rocketFullName = varchar("rocket", 200).nullable()
    val rocketVariant = varchar("rocket_variant", 200).nullable()
    val missionId = integer("mission_id").nullable()
    val missionLaunchLibId = integer("mission_launch_library_id").nullable()
    val missionName = varchar("mission", 255).nullable()
    val missionDescription = text("mission_description").nullable()
    val missionLaunchDesignator = varchar("mission_launch_designator", 255).nullable()
    val missionType = text("mission_type").nullable()
    val missionOrbitId = integer("mission_orbit_id").nullable()
    val missionOrbitName = varchar("mission_orbit_name", 50).nullable()
    val missionOrbitAbbrev = varchar("mission_orbit_abbrev", 30).nullable()
    val padId = integer("pad_id")
    val padAgencyId = integer("pad_agency_id").nullable()
    val padName = varchar("pad", 255)
    val padInfoUrl = varchar("pad_info_url", 200).nullable()
    val padWikiUrl = varchar("pad_wiki_url", 200).nullable()
    val padMapUrl = varchar("pad_map_url", 200).nullable()
    val padLatitude = varchar("pad_latitude", 30).nullable()
    val padLongitude = varchar("pad_longitude", 30).nullable()
    val padMapImageUrl = text("pad_map_image_url").nullable()
    val padTotalLaunchCount = text("pad_total_launch_count")
    val padLocationId = integer("pad_location_id").nullable()
    val padLocationName = varchar("pad_location", 255).nullable()
    val padLocationCountryCode = varchar("pad_location_country_code", 255).nullable()
    val padLocationMapImageUrl = text("pad_location_map_image_url").nullable()
    val padLocationTotalLaunchCount = text("pad_location_total_launch_count").nullable()
    val padLocationTotalLandingCount = text("pad_location_total_landing_count").nullable()
}

open class LaunchLibH2(private val database: Database) {
    fun insertLaunch(launch: Launch) {
        transaction(database) {
            LaunchLibLaunches.insert {
                it[uuid] = launch.uuid
                it[launchLibId] = launch.launchLibId
                it[slug] = launch.slug
                it[name] = launch.name
                it[statusId] = launch.status.id
                it[statusDescription] = launch.status.description
                it[netTime] = launch.netTime
                it[windowEndTime] = launch.windowEndTime
                it[windowStartTime] = launch.windowStartTime
                it[inHold] = launch.inHold
                it[timeTBD] = launch.timeTBD
                it[dateTBD] = launch.dateTBD
                it[probability] = launch.probability
                it[holdReason] = launch.holdReason
                it[failReason] = launch.failReason
                it[hashtag] = launch.hashtag
                it[infoUrls] = launch.infoUrls
                it[videoUrls] = launch.videoUrls
                it[hasWebcast] = launch.hasWebcast
                it[imageUrl] = launch.imageUrl
                it[infographicUrl] = launch.infographicUrl
                it[agencyId] = launch.agency.id
                it[agencyName] = launch.agency.name
                it[agencyType] = launch.agency.type
                it[rocketId] = launch.rocket.id
                it[rocketIdAlt] = launch.rocket.configuration.id
                it[rocketLaunchLibId] = launch.rocket.configuration.launchLibId
                it[rocketName] = launch.rocket.configuration.name
                it[rocketFamily] = launch.rocket.configuration.family
                it[rocketFullName] = launch.rocket.configuration.fullName
                it[rocketVariant] = launch.rocket.configuration.variant
                it[missionId] = launch.mission?.id
                it[missionLaunchLibId] = launch.mission?.launchLibId
                it[missionName] = launch.mission?.name
                it[missionDescription] = launch.mission?.description
                it[missionLaunchDesignator] = launch.mission?.launchDesignator
                it[missionType] = launch.mission?.type
                it[missionOrbitId] = launch.mission?.orbit?.id
                it[missionOrbitName] = launch.mission?.orbit?.name
                it[missionOrbitAbbrev] = launch.mission?.orbit?.abbrev
                it[padId] = launch.pad.id
                it[padAgencyId] = launch.pad.agencyId
                it[padName] = launch.pad.name
                it[padInfoUrl] = launch.pad.infoUrl
                it[padWikiUrl] = launch.pad.wikiUrl
                it[padMapUrl] = launch.pad.mapUrl
                it[padLatitude] = launch.pad.latitude
                it[padLongitude] = launch.pad.longitude
                it[padMapImageUrl] = launch.pad.mapImageUrl
                it[padTotalLaunchCount] = launch.pad.totalLaunchCount
                it[padLocationId] = launch.pad.location?.id
                it[padLocationName] = launch.pad.location?.name
                it[padLocationCountryCode] = launch.pad.location?.countryCode
                it[padLocationMapImageUrl] = launch.pad.location?.mapImageUrl
                it[padLocationTotalLaunchCount] = launch.pad.location?.totalLaunchCount
                it[padLocationTotalLandingCount] = launch.pad.location?.totalLandingCount
            }
        }
    }

    fun updateLaunch(launch: Launch) {
        transaction(database) {
            LaunchLibLaunches.update {
                it[uuid] = launch.uuid
                it[launchLibId] = launch.launchLibId
                it[slug] = launch.slug
                it[name] = launch.name
                it[statusId] = launch.status.id
                it[statusDescription] = launch.status.description
                it[netTime] = launch.netTime
                it[windowEndTime] = launch.windowEndTime
                it[windowStartTime] = launch.windowStartTime
                it[inHold] = launch.inHold
                it[timeTBD] = launch.timeTBD
                it[dateTBD] = launch.dateTBD
                it[probability] = launch.probability
                it[holdReason] = launch.holdReason
                it[failReason] = launch.failReason
                it[hashtag] = launch.hashtag
                it[infoUrls] = launch.infoUrls
                it[videoUrls] = launch.videoUrls
                it[hasWebcast] = launch.hasWebcast
                it[imageUrl] = launch.imageUrl
                it[infographicUrl] = launch.infographicUrl
                it[agencyId] = launch.agency.id
                it[agencyName] = launch.agency.name
                it[agencyType] = launch.agency.type
                it[rocketId] = launch.rocket.id
                it[rocketIdAlt] = launch.rocket.configuration.id
                it[rocketLaunchLibId] = launch.rocket.configuration.launchLibId
                it[rocketName] = launch.rocket.configuration.name
                it[rocketFamily] = launch.rocket.configuration.family
                it[rocketFullName] = launch.rocket.configuration.fullName
                it[rocketVariant] = launch.rocket.configuration.variant
                it[missionId] = launch.mission?.id
                it[missionLaunchLibId] = launch.mission?.launchLibId
                it[missionName] = launch.mission?.name
                it[missionDescription] = launch.mission?.description
                it[missionLaunchDesignator] = launch.mission?.launchDesignator
                it[missionType] = launch.mission?.type
                it[missionOrbitId] = launch.mission?.orbit?.id
                it[missionOrbitName] = launch.mission?.orbit?.name
                it[missionOrbitAbbrev] = launch.mission?.orbit?.abbrev
                it[padId] = launch.pad.id
                it[padAgencyId] = launch.pad.agencyId
                it[padName] = launch.pad.name
                it[padInfoUrl] = launch.pad.infoUrl
                it[padWikiUrl] = launch.pad.wikiUrl
                it[padMapUrl] = launch.pad.mapUrl
                it[padLatitude] = launch.pad.latitude
                it[padLongitude] = launch.pad.longitude
                it[padMapImageUrl] = launch.pad.mapImageUrl
                it[padTotalLaunchCount] = launch.pad.totalLaunchCount
                it[padLocationId] = launch.pad.location?.id
                it[padLocationName] = launch.pad.location?.name
                it[padLocationCountryCode] = launch.pad.location?.countryCode
                it[padLocationMapImageUrl] = launch.pad.location?.mapImageUrl
                it[padLocationTotalLaunchCount] = launch.pad.location?.totalLaunchCount
                it[padLocationTotalLandingCount] = launch.pad.location?.totalLandingCount
            }
        }
    }

    // Return true if none of the arguments are true.
    private fun <T> noneAreNull(vararg values: T?): Boolean {
        values.forEach {
            if (it == null) return false
        }
        return true
    }

    fun getLaunch(launchUUID: String): Launch? {
        var result: Launch? = null
        transaction(database) {
            LaunchLibLaunches.select {
                LaunchLibLaunches.uuid eq launchUUID
            }.withDistinct().map {
                result = Launch(
                    uuid = it[LaunchLibLaunches.uuid],
                    launchLibId = it[LaunchLibLaunches.launchLibId],
                    slug = it[LaunchLibLaunches.slug],
                    name = it[LaunchLibLaunches.name],
                    status = Status(
                        id = it[LaunchLibLaunches.statusId],
                        description = it[LaunchLibLaunches.statusDescription]
                    ),
                    netTime = it[LaunchLibLaunches.netTime],
                    windowEndTime = it[LaunchLibLaunches.windowEndTime],
                    windowStartTime = it[LaunchLibLaunches.windowStartTime],
                    inHold = it[LaunchLibLaunches.inHold],
                    timeTBD = it[LaunchLibLaunches.timeTBD],
                    dateTBD = it[LaunchLibLaunches.dateTBD],
                    probability = it[LaunchLibLaunches.probability],
                    holdReason = it[LaunchLibLaunches.holdReason],
                    failReason = it[LaunchLibLaunches.failReason],
                    hashtag = it[LaunchLibLaunches.hashtag],
                    infoUrls = it[LaunchLibLaunches.infoUrls],
                    videoUrls = it[LaunchLibLaunches.videoUrls],
                    hasWebcast = it[LaunchLibLaunches.hasWebcast],
                    imageUrl = it[LaunchLibLaunches.imageUrl],
                    infographicUrl = it[LaunchLibLaunches.infographicUrl],
                    agency = Agency(
                        id = it[LaunchLibLaunches.agencyId],
                        name = it[LaunchLibLaunches.agencyName],
                        type = it[LaunchLibLaunches.agencyType]
                    ),
                    rocket = Rocket(
                        id = it[LaunchLibLaunches.rocketId],
                        configuration = RocketDetails(
                            id = it[LaunchLibLaunches.rocketIdAlt],
                            launchLibId = it[LaunchLibLaunches.rocketLaunchLibId],
                            name = it[LaunchLibLaunches.rocketName],
                            family = it[LaunchLibLaunches.rocketFamily],
                            fullName = it[LaunchLibLaunches.rocketFullName],
                            variant = it[LaunchLibLaunches.rocketVariant]
                        )
                    ),
                    mission = if (noneAreNull(
                            it[LaunchLibLaunches.missionId],
                            it[LaunchLibLaunches.missionName],
                            it[LaunchLibLaunches.missionDescription],
                            it[LaunchLibLaunches.missionType]
                        )
                    ) Mission(
                        id = it[LaunchLibLaunches.missionId]!!,
                        launchLibId = it[LaunchLibLaunches.missionLaunchLibId],
                        name = it[LaunchLibLaunches.missionName]!!,
                        description = it[LaunchLibLaunches.missionDescription]!!,
                        launchDesignator = it[LaunchLibLaunches.missionLaunchDesignator],
                        type = it[LaunchLibLaunches.missionType]!!,
                        orbit = if (noneAreNull(
                                it[LaunchLibLaunches.missionOrbitId],
                                it[LaunchLibLaunches.missionOrbitName],
                                it[LaunchLibLaunches.missionOrbitAbbrev]
                            )
                        ) Orbit(
                            id = it[LaunchLibLaunches.missionOrbitId]!!,
                            name = it[LaunchLibLaunches.missionOrbitName]!!,
                            abbrev = it[LaunchLibLaunches.missionOrbitAbbrev]!!
                        ) else null
                    ) else null,
                    pad = Pad(
                        id = it[LaunchLibLaunches.padId],
                        agencyId = it[LaunchLibLaunches.padAgencyId],
                        name = it[LaunchLibLaunches.padName],
                        infoUrl = it[LaunchLibLaunches.padInfoUrl],
                        wikiUrl = it[LaunchLibLaunches.padWikiUrl],
                        mapUrl = it[LaunchLibLaunches.padMapUrl],
                        latitude = it[LaunchLibLaunches.padLatitude],
                        longitude = it[LaunchLibLaunches.padLongitude],
                        mapImageUrl = it[LaunchLibLaunches.padMapImageUrl],
                        totalLaunchCount = it[LaunchLibLaunches.padTotalLaunchCount],
                        location = if (noneAreNull(
                                it[LaunchLibLaunches.padLocationId],
                                it[LaunchLibLaunches.padLocationName],
                                it[LaunchLibLaunches.padLocationCountryCode],
                                it[LaunchLibLaunches.padLocationTotalLaunchCount],
                                it[LaunchLibLaunches.padLocationTotalLandingCount]
                            )
                        ) Location(
                            id = it[LaunchLibLaunches.padLocationId]!!,
                            name = it[LaunchLibLaunches.padLocationName]!!,
                            countryCode = it[LaunchLibLaunches.padLocationCountryCode]!!,
                            mapImageUrl = it[LaunchLibLaunches.padLocationMapImageUrl],
                            totalLaunchCount = it[LaunchLibLaunches.padLocationTotalLaunchCount]!!,
                            totalLandingCount = it[LaunchLibLaunches.padLocationTotalLandingCount]!!
                        ) else null
                    )
                )
            }

        }
        return result
    }
}