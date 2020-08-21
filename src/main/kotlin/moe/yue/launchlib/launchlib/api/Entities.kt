package moe.yue.launchlib.launchlib.api

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

// This file contains basic json objects from http request used by the project
@Serializable
data class LaunchLibResult(
    @SerialName("count") val count: Int,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<LaunchLibLaunch>
)

@Serializable
data class LaunchLibLaunch(
    @SerialName("id") val uuid: String,
    @SerialName("url") val url: String? = null,
    @SerialName("launch_library_id") val launchLibId: Int? = null,
    @SerialName("slug") val slug: String,
    @SerialName("name") val name: String,
    @SerialName("status") val status: LaunchLibStatus,
    @SerialName("net") val netTime: String? = null,
    @SerialName("window_end") val windowEndTime: String? = null,
    @SerialName("window_start") val windowStartTime: String? = null,
    @SerialName("inhold") val inHold: Boolean? = null,
    @SerialName("tbdtime") val timeTBD: Boolean? = null,
    @SerialName("tbddate") val dateTBD: Boolean? = null,
    @SerialName("probability") val probability: Int? = null,
    @SerialName("holdreason") val holdReason: String? = null,
    @SerialName("failreason") val failReason: String? = null,
    @SerialName("hashtag") val hashtag: String? = null,
    @SerialName("infoURLs") val infoUrls: String? = null,
    @SerialName("vidURLs") val videoUrls: String? = null,
    @SerialName("webcast_live") val hasWebcast: Boolean,
    @SerialName("image") val imageUrl: String? = null,
    @SerialName("infographic") val infographicUrl: String? = null,
    @SerialName("launch_service_provider") val agency: LaunchLibAgency? = null,
    @SerialName("rocket") val rocket: LaunchLibRocket,
    @SerialName("mission") val mission: LaunchLibMission? = null,
    @SerialName("pad") val pad: LaunchLibPad
)

@Serializable
data class LaunchLibStatus(
    @SerialName("id") val id: Int,
    @SerialName("name") val description: String
)

@Serializable
data class LaunchLibAgency(
    @SerialName("id") val id: Int,
    @SerialName("url") val url: String? = null,
    @SerialName("name") val name: String,
    @SerialName("type") val type: String? = null
)

@Serializable
data class LaunchLibRocket(
    @SerialName("id") val id: Int,
    @SerialName("configuration") val configuration: LaunchLibRocketDetails
)

@Serializable
data class LaunchLibRocketDetails(
    @SerialName("id") val id: Int? = null,
    @SerialName("launch_library_id") val launchLibId: Int? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("family") val family: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("variant") val variant: String? = null
)

@Serializable
data class LaunchLibMission(
    @SerialName("id") val id: Int,
    @SerialName("launch_library_id") val launchLibId: Int? = null,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("launch_designator") val launchDesignator: String? = null,
    @SerialName("type") val type: String,
    @SerialName("orbit") val orbit: LaunchLibOrbit? = null
)

@Serializable
data class LaunchLibOrbit(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("abbrev") val abbrev: String
)

@Serializable
data class LaunchLibPad(
    @SerialName("id") val id: Int,
    @SerialName("url") val url: String? = null,
    @SerialName("agency_id") val agencyId: Int? = null,
    @SerialName("name") val name: String,
    @SerialName("info_url") val infoUrl: String? = null,
    @SerialName("wiki_url") val wikiUrl: String? = null,
    @SerialName("map_url") val mapUrl: String? = null,
    @SerialName("latitude") val latitude: String? = null,
    @SerialName("longitude") val longitude: String? = null,
    @SerialName("map_image") val mapImageUrl: String? = null,
    @SerialName("total_launch_count") val totalLaunchCount: String,
    @SerialName("location") val location: LaunchLibLocation? = null
)

@Serializable
data class LaunchLibLocation(
    @SerialName("id") val id: Int,
    @SerialName("url") val url: String? = null,
    @SerialName("name") val name: String,
    @SerialName("country_code") val countryCode: String,
    @SerialName("map_image") val mapImageUrl: String? = null,
    @SerialName("total_launch_count") val totalLaunchCount: String,
    @SerialName("total_landing_count") val totalLandingCount: String
)