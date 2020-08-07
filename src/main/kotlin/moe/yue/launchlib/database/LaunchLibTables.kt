package moe.yue.launchlib.database


import moe.yue.launchlib.launchlib.api.*
import moe.yue.launchlib.timeUtils
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object H2Launches : Table("launches") {
    val uuid = varchar("uuid", 36).uniqueIndex()
    val launchLibId = integer("launch_library_id").nullable()
    val slug = varchar("slug", 2048)
    val name = varchar("name", 2048)
    val statusId = integer("status_id")
    val statusDescription = varchar("status", 255)
    val netEpochTime = long("net_time").nullable()
    val windowEndEpochTime = long("window_end_time").nullable()
    val windowStartEpochTime = long("window_start_time").nullable()
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

// The data class for H2Launches and the flatten LaunchLibLaunch@launchlib.api.EntitiesKt
data class H2Launch(
    val uuid: String,
    val launchLibId: Int? = null,
    val slug: String,
    val name: String,
    val statusId: Int,
    val statusDescription: String,
    val netEpochTime: Long? = null,
    val windowEndEpochTime: Long? = null,
    val windowStartEpochTime: Long? = null,
    val inHold: Boolean? = null,
    val timeTBD: Boolean? = null,
    val dateTBD: Boolean? = null,
    val probability: Int? = null,
    val holdReason: String? = null,
    val failReason: String? = null,
    val hashtag: String? = null,
    val infoUrls: String? = null,
    val videoUrls: String? = null,
    val hasWebcast: Boolean,
    val imageUrl: String? = null,
    val infographicUrl: String? = null,
    val agencyId: Int,
    val agencyName: String,
    val agencyType: String? = null,
    val rocketId: Int,
    val rocketIdAlt: Int? = null,
    val rocketLaunchLibId: Int? = null,
    val rocketName: String? = null,
    val rocketFamily: String? = null,
    val rocketFullName: String? = null,
    val rocketVariant: String? = null,
    val missionId: Int? = null,
    val missionLaunchLibId: Int? = null,
    val missionName: String? = null,
    val missionDescription: String? = null,
    val missionLaunchDesignator: String? = null,
    val missionType: String? = null,
    val missionOrbitId: Int? = null,
    val missionOrbitName: String? = null,
    val missionOrbitAbbrev: String? = null,
    val padId: Int,
    val padAgencyId: Int? = null,
    val padName: String,
    val padInfoUrl: String? = null,
    val padWikiUrl: String? = null,
    val padMapUrl: String? = null,
    val padLatitude: String? = null,
    val padLongitude: String? = null,
    val padMapImageUrl: String? = null,
    val padTotalLaunchCount: String,
    val padLocationId: Int? = null,
    val padLocationName: String? = null,
    val padLocationCountryCode: String? = null,
    val padLocationMapImageUrl: String? = null,
    val padLocationTotalLaunchCount: String? = null,
    val padLocationTotalLandingCount: String? = null
)


open class LaunchLibH2(private val database: Database) {
    fun insertLaunch(launchLibLaunch: LaunchLibLaunch) {
        transaction(database) {
            H2Launches.insert {
                it[uuid] = launchLibLaunch.uuid
                it[launchLibId] = launchLibLaunch.launchLibId
                it[slug] = launchLibLaunch.slug
                it[name] = launchLibLaunch.name
                it[statusId] = launchLibLaunch.status.id
                it[statusDescription] = launchLibLaunch.status.description
                it[netEpochTime] = launchLibLaunch.netTime.run { timeUtils.toEpochTime(this) }
                it[windowEndEpochTime] = launchLibLaunch.windowEndTime.run { timeUtils.toEpochTime(this) }
                it[windowStartEpochTime] = launchLibLaunch.windowStartTime.run { timeUtils.toEpochTime(this) }
                it[inHold] = launchLibLaunch.inHold
                it[timeTBD] = launchLibLaunch.timeTBD
                it[dateTBD] = launchLibLaunch.dateTBD
                it[probability] = launchLibLaunch.probability
                it[holdReason] = launchLibLaunch.holdReason
                it[failReason] = launchLibLaunch.failReason
                it[hashtag] = launchLibLaunch.hashtag
                it[infoUrls] = launchLibLaunch.infoUrls
                it[videoUrls] = launchLibLaunch.videoUrls
                it[hasWebcast] = launchLibLaunch.hasWebcast
                it[imageUrl] = launchLibLaunch.imageUrl
                it[infographicUrl] = launchLibLaunch.infographicUrl
                it[agencyId] = launchLibLaunch.agency.id
                it[agencyName] = launchLibLaunch.agency.name
                it[agencyType] = launchLibLaunch.agency.type
                it[rocketId] = launchLibLaunch.rocket.id
                it[rocketIdAlt] = launchLibLaunch.rocket.configuration.id
                it[rocketLaunchLibId] = launchLibLaunch.rocket.configuration.launchLibId
                it[rocketName] = launchLibLaunch.rocket.configuration.name
                it[rocketFamily] = launchLibLaunch.rocket.configuration.family
                it[rocketFullName] = launchLibLaunch.rocket.configuration.fullName
                it[rocketVariant] = launchLibLaunch.rocket.configuration.variant
                it[missionId] = launchLibLaunch.mission?.id
                it[missionLaunchLibId] = launchLibLaunch.mission?.launchLibId
                it[missionName] = launchLibLaunch.mission?.name
                it[missionDescription] = launchLibLaunch.mission?.description
                it[missionLaunchDesignator] = launchLibLaunch.mission?.launchDesignator
                it[missionType] = launchLibLaunch.mission?.type
                it[missionOrbitId] = launchLibLaunch.mission?.orbit?.id
                it[missionOrbitName] = launchLibLaunch.mission?.orbit?.name
                it[missionOrbitAbbrev] = launchLibLaunch.mission?.orbit?.abbrev
                it[padId] = launchLibLaunch.pad.id
                it[padAgencyId] = launchLibLaunch.pad.agencyId
                it[padName] = launchLibLaunch.pad.name
                it[padInfoUrl] = launchLibLaunch.pad.infoUrl
                it[padWikiUrl] = launchLibLaunch.pad.wikiUrl
                it[padMapUrl] = launchLibLaunch.pad.mapUrl
                it[padLatitude] = launchLibLaunch.pad.latitude
                it[padLongitude] = launchLibLaunch.pad.longitude
                it[padMapImageUrl] = launchLibLaunch.pad.mapImageUrl
                it[padTotalLaunchCount] = launchLibLaunch.pad.totalLaunchCount
                it[padLocationId] = launchLibLaunch.pad.location?.id
                it[padLocationName] = launchLibLaunch.pad.location?.name
                it[padLocationCountryCode] = launchLibLaunch.pad.location?.countryCode
                it[padLocationMapImageUrl] = launchLibLaunch.pad.location?.mapImageUrl
                it[padLocationTotalLaunchCount] = launchLibLaunch.pad.location?.totalLaunchCount
                it[padLocationTotalLandingCount] = launchLibLaunch.pad.location?.totalLandingCount
            }
        }
    }

    fun updateLaunch(launchLibLaunch: LaunchLibLaunch) {
        transaction(database) {
            H2Launches.update {
                it[uuid] = launchLibLaunch.uuid
                it[launchLibId] = launchLibLaunch.launchLibId
                it[slug] = launchLibLaunch.slug
                it[name] = launchLibLaunch.name
                it[statusId] = launchLibLaunch.status.id
                it[statusDescription] = launchLibLaunch.status.description
                it[netEpochTime] = launchLibLaunch.netTime.run { timeUtils.toEpochTime(this) }
                it[windowEndEpochTime] = launchLibLaunch.windowEndTime.run { timeUtils.toEpochTime(this) }
                it[windowStartEpochTime] = launchLibLaunch.windowStartTime.run { timeUtils.toEpochTime(this) }
                it[inHold] = launchLibLaunch.inHold
                it[timeTBD] = launchLibLaunch.timeTBD
                it[dateTBD] = launchLibLaunch.dateTBD
                it[probability] = launchLibLaunch.probability
                it[holdReason] = launchLibLaunch.holdReason
                it[failReason] = launchLibLaunch.failReason
                it[hashtag] = launchLibLaunch.hashtag
                it[infoUrls] = launchLibLaunch.infoUrls
                it[videoUrls] = launchLibLaunch.videoUrls
                it[hasWebcast] = launchLibLaunch.hasWebcast
                it[imageUrl] = launchLibLaunch.imageUrl
                it[infographicUrl] = launchLibLaunch.infographicUrl
                it[agencyId] = launchLibLaunch.agency.id
                it[agencyName] = launchLibLaunch.agency.name
                it[agencyType] = launchLibLaunch.agency.type
                it[rocketId] = launchLibLaunch.rocket.id
                it[rocketIdAlt] = launchLibLaunch.rocket.configuration.id
                it[rocketLaunchLibId] = launchLibLaunch.rocket.configuration.launchLibId
                it[rocketName] = launchLibLaunch.rocket.configuration.name
                it[rocketFamily] = launchLibLaunch.rocket.configuration.family
                it[rocketFullName] = launchLibLaunch.rocket.configuration.fullName
                it[rocketVariant] = launchLibLaunch.rocket.configuration.variant
                it[missionId] = launchLibLaunch.mission?.id
                it[missionLaunchLibId] = launchLibLaunch.mission?.launchLibId
                it[missionName] = launchLibLaunch.mission?.name
                it[missionDescription] = launchLibLaunch.mission?.description
                it[missionLaunchDesignator] = launchLibLaunch.mission?.launchDesignator
                it[missionType] = launchLibLaunch.mission?.type
                it[missionOrbitId] = launchLibLaunch.mission?.orbit?.id
                it[missionOrbitName] = launchLibLaunch.mission?.orbit?.name
                it[missionOrbitAbbrev] = launchLibLaunch.mission?.orbit?.abbrev
                it[padId] = launchLibLaunch.pad.id
                it[padAgencyId] = launchLibLaunch.pad.agencyId
                it[padName] = launchLibLaunch.pad.name
                it[padInfoUrl] = launchLibLaunch.pad.infoUrl
                it[padWikiUrl] = launchLibLaunch.pad.wikiUrl
                it[padMapUrl] = launchLibLaunch.pad.mapUrl
                it[padLatitude] = launchLibLaunch.pad.latitude
                it[padLongitude] = launchLibLaunch.pad.longitude
                it[padMapImageUrl] = launchLibLaunch.pad.mapImageUrl
                it[padTotalLaunchCount] = launchLibLaunch.pad.totalLaunchCount
                it[padLocationId] = launchLibLaunch.pad.location?.id
                it[padLocationName] = launchLibLaunch.pad.location?.name
                it[padLocationCountryCode] = launchLibLaunch.pad.location?.countryCode
                it[padLocationMapImageUrl] = launchLibLaunch.pad.location?.mapImageUrl
                it[padLocationTotalLaunchCount] = launchLibLaunch.pad.location?.totalLaunchCount
                it[padLocationTotalLandingCount] = launchLibLaunch.pad.location?.totalLandingCount
            }
        }
    }

    private fun ResultRow.toH2Launch() = H2Launch(
        uuid = this[H2Launches.uuid],
        launchLibId = this[H2Launches.launchLibId],
        slug = this[H2Launches.slug],
        name = this[H2Launches.name],
        statusId = this[H2Launches.statusId],
        statusDescription = this[H2Launches.statusDescription],
        netEpochTime = this[H2Launches.netEpochTime],
        windowEndEpochTime = this[H2Launches.windowEndEpochTime],
        windowStartEpochTime = this[H2Launches.windowStartEpochTime],
        inHold = this[H2Launches.inHold],
        timeTBD = this[H2Launches.timeTBD],
        dateTBD = this[H2Launches.dateTBD],
        probability = this[H2Launches.probability],
        holdReason = this[H2Launches.holdReason],
        failReason = this[H2Launches.failReason],
        hashtag = this[H2Launches.hashtag],
        infoUrls = this[H2Launches.infoUrls],
        videoUrls = this[H2Launches.videoUrls],
        hasWebcast = this[H2Launches.hasWebcast],
        imageUrl = this[H2Launches.imageUrl],
        infographicUrl = this[H2Launches.infographicUrl],
        agencyId = this[H2Launches.agencyId],
        agencyName = this[H2Launches.agencyName],
        agencyType = this[H2Launches.agencyType],
        rocketId = this[H2Launches.rocketId],
        rocketIdAlt = this[H2Launches.rocketIdAlt],
        rocketLaunchLibId = this[H2Launches.rocketLaunchLibId],
        rocketName = this[H2Launches.rocketName],
        rocketFamily = this[H2Launches.rocketFamily],
        rocketFullName = this[H2Launches.rocketFullName],
        rocketVariant = this[H2Launches.rocketVariant],
        missionId = this[H2Launches.missionId],
        missionLaunchLibId = this[H2Launches.missionLaunchLibId],
        missionName = this[H2Launches.missionName],
        missionDescription = this[H2Launches.missionDescription],
        missionLaunchDesignator = this[H2Launches.missionLaunchDesignator],
        missionType = this[H2Launches.missionType],
        missionOrbitId = this[H2Launches.missionOrbitId],
        missionOrbitName = this[H2Launches.missionOrbitName],
        missionOrbitAbbrev = this[H2Launches.missionOrbitAbbrev],
        padId = this[H2Launches.padId],
        padAgencyId = this[H2Launches.padAgencyId],
        padName = this[H2Launches.padName],
        padInfoUrl = this[H2Launches.padInfoUrl],
        padWikiUrl = this[H2Launches.padWikiUrl],
        padMapUrl = this[H2Launches.padMapUrl],
        padLatitude = this[H2Launches.padLatitude],
        padLongitude = this[H2Launches.padLongitude],
        padMapImageUrl = this[H2Launches.padMapImageUrl],
        padTotalLaunchCount = this[H2Launches.padTotalLaunchCount],
        padLocationId = this[H2Launches.padLocationId],
        padLocationName = this[H2Launches.padLocationName],
        padLocationCountryCode = this[H2Launches.padLocationCountryCode],
        padLocationMapImageUrl = this[H2Launches.padLocationMapImageUrl],
        padLocationTotalLaunchCount = this[H2Launches.padLocationTotalLaunchCount],
        padLocationTotalLandingCount = this[H2Launches.padLocationTotalLandingCount]
    )

    fun getLaunch(launchUUID: String): H2Launch? {
        var result: H2Launch? = null
        transaction(database) {
            H2Launches.select {
                H2Launches.uuid eq launchUUID
            }.withDistinct().map {
                result = it.toH2Launch()
            }
        }
        return result
    }

    fun getRecentLaunches(
        fromPreviousSeconds: Int = timeUtils.hourToSecond(1.5),
        toFollowingSeconds: Int = fromPreviousSeconds
    ): List<H2Launch> {
        val result = mutableListOf<H2Launch>()
        transaction(database) {
            H2Launches.select {
                (H2Launches.netEpochTime greater timeUtils.getNow() - fromPreviousSeconds) and
                        (H2Launches.netEpochTime less timeUtils.getNow() + toFollowingSeconds)
            }.map {
                result += it.toH2Launch()
            }
        }
        return result
    }
}