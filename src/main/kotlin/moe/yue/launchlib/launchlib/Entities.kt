package moe.yue.launchlib.launchlib

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


@Serializable
data class Result(
    @SerialName("count") val count: Int,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<Launch>
)

@Serializable
data class Launch(
    @SerialName("id") val uuid: String,
    @SerialName("url") val url: String,
    @SerialName("launch_library_id") val launchLibraryId: Int? = null,
    @SerialName("slug") val slug: String,
    @SerialName("name") val name: String,
    @SerialName("status") val status: Status,
    @SerialName("net") val net: String? = null,
    @SerialName("window_end") val windowEnd: String? = null,
    @SerialName("window_start") val windowStart: String? = null,
    @SerialName("inhold") val inHold: Boolean? = null,
    @SerialName("tbdtime") val tbdTime: Boolean? = null,
    @SerialName("tbddate") val tbdDate: Boolean? = null,
    @SerialName("probability") val probability: Int? = null,
    @SerialName("holdreason") val holdReason: String? = null,
    @SerialName("failreason") val failReason: String? = null,
    @SerialName("hashtag") val hashtag: String? = null,
    @SerialName("launch_service_provider") val launchServiceProvider: Agency,
    @SerialName("rocket") val rocket: Rocket,
    @SerialName("mission") val mission: Mission? = null,
    @SerialName("pad") val pad: Pad,
    @SerialName("webcast_live") val webcastLive: Boolean,
    @SerialName("image") val image: String? = null,
    @SerialName("infographic") val infographic: String? = null
)

@Serializable
data class Status(
    @SerialName("id") val id: Int,
    @SerialName("name") val description: String
)

@Serializable
data class Agency(
    @SerialName("id") val id: Int,
    @SerialName("url") val url: String,
    @SerialName("name") val name: String,
    @SerialName("type") val type: String? = null
)

@Serializable
data class Rocket(
    @SerialName("id") val id: Int,
    @SerialName("configuration") val configuration: RocketDetails
)

@Serializable
data class RocketDetails(
    @SerialName("id") val id: Int? = null,
    @SerialName("launch_library_id") val launchLibraryId: Int? = null,
    @SerialName("url") val url: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("family") val family: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("variant") val variant: String? = null
)

@Serializable
data class Mission(
    @SerialName("id") val id: Int,
    @SerialName("launch_library_id") val launchLibraryId: Int? = null,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("launch_designator") val launchDesignator: String? = null,
    @SerialName("type") val type: String,
    @SerialName("orbit") val orbit: Orbit? = null
)

@Serializable
data class Orbit(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("abbrev") val abbrev: String
)

@Serializable
data class Pad(
    @SerialName("id") val id: Int,
    @SerialName("url") val url: String,
    @SerialName("agency_id") val agencyId: Int? = null,
    @SerialName("name") val name: String,
    @SerialName("info_url") val infoUrl: String? = null,
    @SerialName("wiki_url") val wikiUrl: String? = null,
    @SerialName("map_url") val mapUrl: String? = null,
    @SerialName("latitude") val latitude: String? = null,
    @SerialName("longitude") val longitude: String? = null,
    @SerialName("location") val location: Location? = null,
    @SerialName("map_image") val mapImage: String? = null,
    @SerialName("total_launch_count") val totalLaunchCount: Int
)

@Serializable
data class Location(
    @SerialName("id") val id: Int,
    @SerialName("url") val url: String,
    @SerialName("name") val name: String,
    @SerialName("country_code") val countryCode: String,
    @SerialName("map_image") val mapImage: String? = null,
    @SerialName("total_launch_count") val totalLaunchCount: Int,
    @SerialName("total_landing_count") val totalLandingCount: Int
)