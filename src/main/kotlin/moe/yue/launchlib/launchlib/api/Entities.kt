package moe.yue.launchlib.launchlib.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Data classes generated from json response, header part
@Serializable
data class LaunchLibResult(
    @SerialName("count") val count: Int,
    @SerialName("next") val nextPageUrl: String? = null,
    @SerialName("previous") val previousPageUrl: String? = null,
    @SerialName("results") val results: List<LaunchLibLaunch>
)

// Data classes generated from json response, data part
@Serializable
data class LaunchLibLaunch(
    @SerialName("id") val uuid: String,
    @SerialName("url") val launchLibUrl: String? = null,
    @SerialName("slug") val slug: String,
    @SerialName("name") val name: String,
    @SerialName("last_updated") val lastUpdated: String,
    @SerialName("net") val netTime: String? = null,
    @SerialName("window_end") val windowEndTime: String? = null,
    @SerialName("window_start") val windowStartTime: String? = null,
    @SerialName("probability") val probability: Int? = null,
    @SerialName("holdreason") val holdReason: String? = null,
    @SerialName("failreason") val failReason: String? = null,
    @SerialName("hashtag") val hashtag: String? = null,
    @SerialName("webcast_live") val hasWebcast: Boolean,
    @SerialName("image") val imageUrl: String? = null,
    @SerialName("infographic") val infographicUrl: String? = null,

    @SerialName("status") val status: Status,
    @SerialName("launch_service_provider") val agency: Agency,
    @SerialName("rocket") val rocket: Rocket,
    @SerialName("mission") val mission: Mission? = null,
    @SerialName("pad") val pad: Pad,
//    @SerialName("program") val program: List<Program>
) {
    @Serializable
    data class Status(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
        @SerialName("abbrev") val abbrev: String,
        @SerialName("description") val description: String? = null,
    )

    @Serializable
    data class Agency(
        @SerialName("id") val id: Int,
        @SerialName("url") val launchLibUrl: String? = null,
        @SerialName("name") val name: String,
        @SerialName("type") val type: String? = null
    )

    @Serializable
    data class Rocket(
        @SerialName("id") val id: Int,
        @SerialName("configuration") val configuration: Details
    ) {
        @Serializable
        data class Details(
            @SerialName("id") val id: Int? = null,
            @SerialName("url") val launchLibUrl: String? = null,
            @SerialName("name") val name: String? = null,
            @SerialName("family") val family: String? = null,
            @SerialName("full_name") val fullName: String? = null,
            @SerialName("variant") val variant: String? = null
        )
    }

    @Serializable
    data class Mission(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
        @SerialName("description") val description: String,
        @SerialName("launch_designator") val launchDesignator: String? = null,
        @SerialName("type") val type: String,
        @SerialName("orbit") val orbit: Orbit? = null
    ) {
        @Serializable
        data class Orbit(
            @SerialName("id") val id: Int,
            @SerialName("name") val name: String,
            @SerialName("abbrev") val abbrev: String
        )
    }

    @Serializable
    data class Pad(
        @SerialName("id") val id: Int,
        @SerialName("url") val launchLibUrl: String? = null,
        @SerialName("agency_id") val agencyId: Int? = null,
        @SerialName("name") val name: String,
        @SerialName("info_url") val infoUrl: String? = null,
        @SerialName("wiki_url") val wikiUrl: String? = null,
        @SerialName("map_url") val mapUrl: String? = null,
        @SerialName("latitude") val latitude: String? = null,
        @SerialName("longitude") val longitude: String? = null,
        @SerialName("map_image") val mapImageUrl: String? = null,
        @SerialName("total_launch_count") val totalLaunchCount: String,
        @SerialName("location") val location: Location? = null
    ) {
        @Serializable
        data class Location(
            @SerialName("id") val id: Int,
            @SerialName("url") val launchLibUrl: String? = null,
            @SerialName("name") val name: String,
            @SerialName("country_code") val countryCode: String,
            @SerialName("map_image") val mapImageUrl: String? = null,
            @SerialName("total_launch_count") val totalLaunchCount: String,
            @SerialName("total_landing_count") val totalLandingCount: String
        )
    }

    // Unused information
//    @Serializable
//    data class Program(
//        @SerialName("id") val id: Int,
//        @SerialName("url") val launchLibUrl: String? = null,
//        @SerialName("name") val name: String,
//        @SerialName("description") val description: String,
//        @SerialName("image_url") val imageUrl: String,
//        @SerialName("start_date") val startDate: String,
//        @SerialName("end_date") val endDate: String? = null,
//        @SerialName("info_url") val infoUrl: String? = null,
//        @SerialName("wiki_url") val wikiUrl: String? = null,
//        @SerialName("agencies") val agencies: List<Agency>
//    ) {
//        @Serializable
//        data class Agency(
//            @SerialName("id") val id: Int,
//            @SerialName("url") val launchLibUrl: String? = null,
//            @SerialName("name") val name: String,
//            @SerialName("type") val type: String? = null
//        )
//    }
}
