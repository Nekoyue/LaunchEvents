package moe.yue.launchlib.launchlib.api

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName

// Basic information of all space agencies from https://ll.thespacedevs.com/2.0.0/agencies/
// last updated on Aug 11, 2020
// The integer key is the value of LaunchLibPad.agencyId@launchlib.api.EntitiesKt
// These information are all hard-coded as I modified something:
//      - Changed long lists of european countries in countryCode to "EU"
//      - Removed uncommon space agency abbreviations
@Serializable
data class AgencyInfo(
    @SerialName("name") val name: String,
    @SerialName("abbrev") val abbrev: String? = null,
    @SerialName("featured") val featured: Boolean,
    @SerialName("type") val type: String? = null,
    @SerialName("country_code") val countryCode: String? = null
)

val flags = mapOf(
    "EU" to "🇪🇺",
    "UN" to "🇺🇳",
    "AD" to "🇦🇩",
    "AE" to "🇦🇪",
    "AF" to "🇦🇫",
    "AG" to "🇦🇬",
    "AI" to "🇦🇮",
    "AL" to "🇦🇱",
    "AM" to "🇦🇲",
    "AO" to "🇦🇴",
    "AQ" to "🇦🇶",
    "AR" to "🇦🇷",
    "AS" to "🇦🇸",
    "AT" to "🇦🇹",
    "AU" to "🇦🇺",
    "AW" to "🇦🇼",
    "AX" to "🇦🇽",
    "AZ" to "🇦🇿",
    "BA" to "🇧🇦",
    "BB" to "🇧🇧",
    "BD" to "🇧🇩",
    "BE" to "🇧🇪",
    "BF" to "🇧🇫",
    "BG" to "🇧🇬",
    "BH" to "🇧🇭",
    "BI" to "🇧🇮",
    "BJ" to "🇧🇯",
    "BL" to "🇧🇱",
    "BM" to "🇧🇲",
    "BN" to "🇧🇳",
    "BO" to "🇧🇴",
    "BQ" to "🇧🇶",
    "BR" to "🇧🇷",
    "BS" to "🇧🇸",
    "BT" to "🇧🇹",
    "BV" to "🇧🇻",
    "BW" to "🇧🇼",
    "BY" to "🇧🇾",
    "BZ" to "🇧🇿",
    "CA" to "🇨🇦",
    "CC" to "🇨🇨",
    "CD" to "🇨🇩",
    "CF" to "🇨🇫",
    "CG" to "🇨🇬",
    "CH" to "🇨🇭",
    "CI" to "🇨🇮",
    "CK" to "🇨🇰",
    "CL" to "🇨🇱",
    "CM" to "🇨🇲",
    "CN" to "🇨🇳",
    "CO" to "🇨🇴",
    "CR" to "🇨🇷",
    "CU" to "🇨🇺",
    "CV" to "🇨🇻",
    "CW" to "🇨🇼",
    "CX" to "🇨🇽",
    "CY" to "🇨🇾",
    "CZ" to "🇨🇿",
    "DE" to "🇩🇪",
    "DJ" to "🇩🇯",
    "DK" to "🇩🇰",
    "DM" to "🇩🇲",
    "DO" to "🇩🇴",
    "DZ" to "🇩🇿",
    "EC" to "🇪🇨",
    "EE" to "🇪🇪",
    "EG" to "🇪🇬",
    "EH" to "🇪🇭",
    "ER" to "🇪🇷",
    "ES" to "🇪🇸",
    "ET" to "🇪🇹",
    "FI" to "🇫🇮",
    "FJ" to "🇫🇯",
    "FK" to "🇫🇰",
    "FM" to "🇫🇲",
    "FO" to "🇫🇴",
    "FR" to "🇫🇷",
    "GA" to "🇬🇦",
    "GB" to "🇬🇧",
    "GD" to "🇬🇩",
    "GE" to "🇬🇪",
    "GF" to "🇬🇫",
    "GG" to "🇬🇬",
    "GH" to "🇬🇭",
    "GI" to "🇬🇮",
    "GL" to "🇬🇱",
    "GM" to "🇬🇲",
    "GN" to "🇬🇳",
    "GP" to "🇬🇵",
    "GQ" to "🇬🇶",
    "GR" to "🇬🇷",
    "GS" to "🇬🇸",
    "GT" to "🇬🇹",
    "GU" to "🇬🇺",
    "GW" to "🇬🇼",
    "GY" to "🇬🇾",
    "HK" to "🇭🇰",
    "HM" to "🇭🇲",
    "HN" to "🇭🇳",
    "HR" to "🇭🇷",
    "HT" to "🇭🇹",
    "HU" to "🇭🇺",
    "ID" to "🇮🇩",
    "IE" to "🇮🇪",
    "IL" to "🇮🇱",
    "IM" to "🇮🇲",
    "IN" to "🇮🇳",
    "IO" to "🇮🇴",
    "IQ" to "🇮🇶",
    "IR" to "🇮🇷",
    "IS" to "🇮🇸",
    "IT" to "🇮🇹",
    "JE" to "🇯🇪",
    "JM" to "🇯🇲",
    "JO" to "🇯🇴",
    "JP" to "🇯🇵",
    "KE" to "🇰🇪",
    "KG" to "🇰🇬",
    "KH" to "🇰🇭",
    "KI" to "🇰🇮",
    "KM" to "🇰🇲",
    "KN" to "🇰🇳",
    "KP" to "🇰🇵",
    "KR" to "🇰🇷",
    "KW" to "🇰🇼",
    "KY" to "🇰🇾",
    "KZ" to "🇰🇿",
    "LA" to "🇱🇦",
    "LB" to "🇱🇧",
    "LC" to "🇱🇨",
    "LI" to "🇱🇮",
    "LK" to "🇱🇰",
    "LR" to "🇱🇷",
    "LS" to "🇱🇸",
    "LT" to "🇱🇹",
    "LU" to "🇱🇺",
    "LV" to "🇱🇻",
    "LY" to "🇱🇾",
    "MA" to "🇲🇦",
    "MC" to "🇲🇨",
    "MD" to "🇲🇩",
    "ME" to "🇲🇪",
    "MF" to "🇲🇫",
    "MG" to "🇲🇬",
    "MH" to "🇲🇭",
    "MK" to "🇲🇰",
    "ML" to "🇲🇱",
    "MM" to "🇲🇲",
    "MN" to "🇲🇳",
    "MO" to "🇲🇴",
    "MP" to "🇲🇵",
    "MQ" to "🇲🇶",
    "MR" to "🇲🇷",
    "MS" to "🇲🇸",
    "MT" to "🇲🇹",
    "MU" to "🇲🇺",
    "MV" to "🇲🇻",
    "MW" to "🇲🇼",
    "MX" to "🇲🇽",
    "MY" to "🇲🇾",
    "MZ" to "🇲🇿",
    "NA" to "🇳🇦",
    "NC" to "🇳🇨",
    "NE" to "🇳🇪",
    "NF" to "🇳🇫",
    "NG" to "🇳🇬",
    "NI" to "🇳🇮",
    "NL" to "🇳🇱",
    "NO" to "🇳🇴",
    "NP" to "🇳🇵",
    "NR" to "🇳🇷",
    "NU" to "🇳🇺",
    "NZ" to "🇳🇿",
    "OM" to "🇴🇲",
    "PA" to "🇵🇦",
    "PE" to "🇵🇪",
    "PF" to "🇵🇫",
    "PG" to "🇵🇬",
    "PH" to "🇵🇭",
    "PK" to "🇵🇰",
    "PL" to "🇵🇱",
    "PM" to "🇵🇲",
    "PN" to "🇵🇳",
    "PR" to "🇵🇷",
    "PS" to "🇵🇸",
    "PT" to "🇵🇹",
    "PW" to "🇵🇼",
    "PY" to "🇵🇾",
    "QA" to "🇶🇦",
    "RE" to "🇷🇪",
    "RO" to "🇷🇴",
    "RS" to "🇷🇸",
    "RU" to "🇷🇺",
    "RW" to "🇷🇼",
    "SA" to "🇸🇦",
    "SB" to "🇸🇧",
    "SC" to "🇸🇨",
    "SD" to "🇸🇩",
    "SE" to "🇸🇪",
    "SG" to "🇸🇬",
    "SH" to "🇸🇭",
    "SI" to "🇸🇮",
    "SJ" to "🇸🇯",
    "SK" to "🇸🇰",
    "SL" to "🇸🇱",
    "SM" to "🇸🇲",
    "SN" to "🇸🇳",
    "SO" to "🇸🇴",
    "SR" to "🇸🇷",
    "SS" to "🇸🇸",
    "ST" to "🇸🇹",
    "SV" to "🇸🇻",
    "SX" to "🇸🇽",
    "SY" to "🇸🇾",
    "SZ" to "🇸🇿",
    "TC" to "🇹🇨",
    "TD" to "🇹🇩",
    "TF" to "🇹🇫",
    "TG" to "🇹🇬",
    "TH" to "🇹🇭",
    "TJ" to "🇹🇯",
    "TK" to "🇹🇰",
    "TL" to "🇹🇱",
    "TM" to "🇹🇲",
    "TN" to "🇹🇳",
    "TO" to "🇹🇴",
    "TR" to "🇹🇷",
    "TT" to "🇹🇹",
    "TV" to "🇹🇻",
    "TW" to "🇹🇼",
    "TZ" to "🇹🇿",
    "UA" to "🇺🇦",
    "UG" to "🇺🇬",
    "UM" to "🇺🇲",
    "US" to "🇺🇸",
    "UY" to "🇺🇾",
    "UZ" to "🇺🇿",
    "VA" to "🇻🇦",
    "VC" to "🇻🇨",
    "VE" to "🇻🇪",
    "VG" to "🇻🇬",
    "VI" to "🇻🇮",
    "VN" to "🇻🇳",
    "VU" to "🇻🇺",
    "WF" to "🇼🇫",
    "WS" to "🇼🇸",
    "YE" to "🇾🇪",
    "YT" to "🇾🇹",
    "ZA" to "🇿🇦",
    "ZM" to "🇿🇲",
    "ZW" to "🇿🇼",
)

val agencyInfo = mapOf(
    1 to AgencyInfo(
        name = "Belarus Space Agency",
        featured = false,
        type = "Government",
        countryCode = "BY"
    ),
    2 to AgencyInfo(
        name = "Aeronautics and Space Research and Diffusion Center",
        featured = false,
        type = "Government",
        countryCode = "UY"
    ),
    3 to AgencyInfo(
        name = "Mexican Space Agency",
        featured = false,
        type = "Government",
        countryCode = "MX"
    ),
    4 to AgencyInfo(
        name = "Algerian Space Agency",
        featured = false,
        type = "Government",
        countryCode = "DZ"
    ),
    5 to AgencyInfo(
        name = "Asia Pacific Multilateral Cooperation in Space Technology and Applications",
        featured = false,
        type = "Multinational",
        countryCode = "UN"
    ),
    6 to AgencyInfo(
        name = "Asia-Pacific Regional Space Agency Forum",
        featured = false,
        type = "Multinational",
        countryCode = "UN"
    ),
    7 to AgencyInfo(
        name = "Asia-Pacific Space Cooperation Organization",
        featured = false,
        type = "Multinational",
        countryCode = "UN"
    ),
    8 to AgencyInfo(
        name = "Austrian Space Agency",
        featured = false,
        type = "Government",
        countryCode = "AT"
    ),
    9 to AgencyInfo(
        name = "Azerbaijan National Aerospace Agency",
        featured = false,
        type = "Government",
        countryCode = "AZ"
    ),
    10 to AgencyInfo(
        name = "Belgian Institute for Space Aeronomy",
        featured = false,
        type = "Government",
        countryCode = "BE"
    ),
    11 to AgencyInfo(
        name = "Bolivarian Agency for Space Activities",
        featured = false,
        type = "Government",
        countryCode = "VE"
    ),
    12 to AgencyInfo(
        name = "Brazilian Space Agency",
        featured = false,
        type = "Government",
        countryCode = "BR"
    ),
    13 to AgencyInfo(
        name = "UK Space Agency",
        featured = false,
        type = "Government",
        countryCode = "GB"
    ),
    14 to AgencyInfo(
        name = "Bolivian Space Agency",
        featured = false,
        type = "Government",
        countryCode = "BO"
    ),
    15 to AgencyInfo(
        name = "Bulgarian Space Agency",
        featured = false,
        type = "Government",
        countryCode = "BG"
    ),
    16 to AgencyInfo(
        name = "Canadian Space Agency",
        featured = false,
        type = "Government",
        countryCode = "CA"
    ),
    17 to AgencyInfo(
        name = "China National Space Administration",
        featured = true,
        type = "Government",
        countryCode = "CN",
        abbrev = "CNSA"
    ),
    18 to AgencyInfo(
        name = "Colombian Space Commission",
        featured = false,
        type = "Government",
        countryCode = "CO"
    ),
    19 to AgencyInfo(
        name = "Centre for Remote Imaging, Sensing and Processing",
        featured = false,
        type = "Educational",
        countryCode = "SG"
    ),
    20 to AgencyInfo(
        name = "Commonwealth Scientific and Industrial Research Organisation",
        featured = false,
        type = "Government",
        countryCode = "AU"
    ),
    21 to AgencyInfo(
        name = "Consultative Committee for Space Data Systems",
        featured = false,
        type = "Multinational",
        countryCode = "UN"
    ),
    22 to AgencyInfo(
        name = "Committee on Space Research",
        featured = false,
        type = "Multinational",
        countryCode = "UN"
    ),
    23 to AgencyInfo(
        name = "Croatian Space Agency",
        featured = false,
        type = "Government",
        countryCode = "HR"
    ),
    24 to AgencyInfo(
        name = "Ministry of Transport of the Czech Republic - Space Technologies and Satellite Systems Department",
        featured = false,
        type = "Government",
        countryCode = "CZ"
    ),
    25 to AgencyInfo(
        name = "Danish National Space Center",
        featured = false,
        type = "Government",
        countryCode = "DK"
    ),
    26 to AgencyInfo(
        name = "Technical University of Denmark - National Space Institute",
        featured = false,
        type = "Educational",
        countryCode = "DK"
    ),
    27 to AgencyInfo(
        name = "European Space Agency",
        featured = false,
        type = "Multinational",
        countryCode = "EU",
        abbrev = "ESA"
    ),
    28 to AgencyInfo(
        name = "Geo-Informatics and Space Technology Development Agency",
        featured = false,
        type = "Government",
        countryCode = "TH"
    ),
    29 to AgencyInfo(
        name = "German Aerospace Center",
        featured = false,
        type = "Government",
        countryCode = "DE"
    ),
    30 to AgencyInfo(
        name = "Hungarian Space Office",
        featured = false,
        type = "Government",
        countryCode = "HU"
    ),
    31 to AgencyInfo(
        name = "Indian Space Research Organization",
        featured = true,
        type = "Government",
        countryCode = "IN",
        abbrev = "ISRO"
    ),
    32 to AgencyInfo(
        name = "Institute for Space Applications and Remote Sensing",
        featured = false,
        type = "Government",
        countryCode = "GR"
    ),
    33 to AgencyInfo(
        name = "Instituto Nacional de Técnica Aeroespacial",
        featured = false,
        type = "Government",
        countryCode = "ES"
    ),
    34 to AgencyInfo(
        name = "Iranian Space Agency",
        featured = false,
        type = "Government",
        countryCode = "IR"
    ),
    35 to AgencyInfo(
        name = "Israeli Space Agency",
        featured = false,
        type = "Government",
        countryCode = "IL"
    ),
    36 to AgencyInfo(
        name = "Italian Space Agency",
        featured = false,
        type = "Government",
        countryCode = "IT",
        abbrev = "ASI"
    ),
    37 to AgencyInfo(
        name = "Japan Aerospace Exploration Agency",
        featured = true,
        type = "Government",
        countryCode = "JP",
        abbrev = "JAXA"
    ),
    38 to AgencyInfo(
        name = "National Space Agency (KazCosmos)",
        featured = false,
        type = "Government",
        countryCode = "KZ"
    ),
    39 to AgencyInfo(
        name = "Kazakh Space Research Institute",
        featured = false,
        type = "Educational",
        countryCode = "KZ"
    ),
    40 to AgencyInfo(
        name = "Korean Committee of Space Technology",
        featured = false,
        type = "Government",
        countryCode = "KP"
    ),
    41 to AgencyInfo(
        name = "Korea Aerospace Research Institute",
        featured = false,
        type = "Government",
        countryCode = "KR"
    ),
    42 to AgencyInfo(
        name = "Lithuanian Space Association",
        featured = false,
        type = "Government",
        countryCode = "LT"
    ),
    43 to AgencyInfo(
        name = "Malaysian National Space Agency",
        featured = false,
        type = "Government",
        countryCode = "MY"
    ),
    44 to AgencyInfo(
        name = "National Aeronautics and Space Administration",
        featured = true,
        type = "Government",
        countryCode = "US",
        abbrev = "NASA"
    ),
    45 to AgencyInfo(
        name = "National Authority for Remote Sensing and Space Sciences",
        featured = false,
        type = "Government",
        countryCode = "EG"
    ),
    46 to AgencyInfo(
        name = "National Center of Space Research",
        featured = false,
        type = "Government",
        countryCode = "FR"
    ),
    47 to AgencyInfo(
        name = "National Commission for Aerospace Research and Development",
        featured = false,
        type = "Government",
        countryCode = "PE"
    ),
    48 to AgencyInfo(
        name = "National Commission for Space Research",
        featured = false,
        type = "Government",
        countryCode = "AR"
    ),
    49 to AgencyInfo(
        name = "National Space Activities Commission",
        featured = false,
        type = "Government",
        countryCode = "AR"
    ),
    50 to AgencyInfo(
        name = "National Institute of Aeronautics and Space",
        featured = false,
        type = "Government",
        countryCode = "ID"
    ),
    51 to AgencyInfo(
        name = "National Remote Sensing Center of Mongolia",
        featured = false,
        type = "Government",
        countryCode = "MN"
    ),
    52 to AgencyInfo(
        name = "National Remote Sensing Center of Tunisia",
        featured = false,
        type = "Government",
        countryCode = "TN"
    ),
    53 to AgencyInfo(
        name = "Uzbek State Space Research Agency (UzbekCosmos)",
        featured = false,
        type = "Government",
        countryCode = "UZ"
    ),
    54 to AgencyInfo(
        name = "National Space Agency of Ukraine",
        featured = false,
        type = "Government",
        countryCode = "UA",
        abbrev = "SSAU"
    ),
    55 to AgencyInfo(
        name = "National Space Organization",
        featured = false,
        type = "Government",
        countryCode = "TW"
    ),
    56 to AgencyInfo(
        name = "National Space Research and Development Agency",
        featured = false,
        type = "Government",
        countryCode = "NG"
    ),
    57 to AgencyInfo(
        name = "Netherlands Institute for Space Research",
        featured = false,
        type = "Government",
        countryCode = "NL"
    ),
    58 to AgencyInfo(
        name = "Norwegian Space Centre",
        featured = false,
        type = "Government",
        countryCode = "NO"
    ),
    59 to AgencyInfo(
        name = "Pakistan Space and Upper Atmosphere Research Commission",
        featured = false,
        type = "Government",
        countryCode = "PK"
    ),
    60 to AgencyInfo(
        name = "FCT Space Office",
        featured = false,
        type = "Government",
        countryCode = "PT"
    ),
    61 to AgencyInfo(
        name = "Romanian Space Agency",
        featured = false,
        type = "Government",
        countryCode = "RO"
    ),
    62 to AgencyInfo(
        name = "Royal Centre for Remote Sensing",
        featured = false,
        type = "Government",
        countryCode = "MA"
    ),
    63 to AgencyInfo(
        name = "Russian Federal Space Agency",
        featured = true,
        type = "Government",
        countryCode = "RU",
        abbrev = "ROSCOSMOS"
    ),
    64 to AgencyInfo(
        name = "Sri Lanka Space Agency",
        featured = false,
        type = "Government",
        countryCode = "LK"
    ),
    65 to AgencyInfo(
        name = "TUBITAK Space Technologies Research Institute",
        featured = false,
        type = "Government",
        countryCode = "TR"
    ),
    66 to AgencyInfo(
        name = "Soviet Space Program",
        featured = false,
        type = "Government",
        countryCode = "RU"
    ),
    67 to AgencyInfo(
        name = "Space Research and Remote Sensing Organization",
        featured = false,
        type = "Government",
        countryCode = "BD"
    ),
    68 to AgencyInfo(
        name = "Space Research Centre",
        featured = false,
        type = "Government",
        countryCode = "PL"
    ),
    69 to AgencyInfo(
        name = "South African National Space Agency",
        featured = false,
        type = "Government",
        countryCode = "ZA"
    ),
    70 to AgencyInfo(
        name = "Space Research Institute of Saudi Arabia",
        featured = false,
        type = "Government",
        countryCode = "SA"
    ),
    71 to AgencyInfo(
        name = "Swiss Space Office",
        featured = false,
        type = "Government",
        countryCode = "CH"
    ),
    72 to AgencyInfo(
        name = "Turkmenistan National Space Agency",
        featured = false,
        type = "Government",
        countryCode = "TM"
    ),
    73 to AgencyInfo(
        name = "United Nations Office for Outer Space Affairs",
        featured = false,
        type = "Multinational",
        countryCode = "UN"
    ),
    75 to AgencyInfo(
        name = "United Nations Committee on the Peaceful Uses of Outer Space",
        featured = false,
        type = "Multinational",
        countryCode = "UN"
    ),
    76 to AgencyInfo(
        name = "Swedish National Space Board",
        featured = false,
        type = "Government",
        countryCode = "SE"
    ),
    77 to AgencyInfo(
        name = "OHB System",
        featured = false,
        type = "Commercial",
        countryCode = "DE"
    ),
    78 to AgencyInfo(
        name = "Thales Alenia Space",
        featured = false,
        type = "Commercial",
        countryCode = "EU"
    ),
    79 to AgencyInfo(
        name = "JSC Information Satellite Systems",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    80 to AgencyInfo(
        name = "Boeing",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    81 to AgencyInfo(
        name = "Astrium Satellites",
        featured = false,
        type = "Commercial",
        countryCode = "EU"
    ),
    82 to AgencyInfo(
        name = "Lockheed Martin",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    83 to AgencyInfo(
        name = "Space Systems/Loral",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    84 to AgencyInfo(
        name = "Amsat",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    85 to AgencyInfo(
        name = "Astronautic Technology Sdn Bhd",
        featured = false,
        type = "Commercial",
        countryCode = "MY"
    ),
    86 to AgencyInfo(
        name = "Ball Aerospace & Technologies Corp.",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    87 to AgencyInfo(
        name = "British Aerospace",
        featured = false,
        type = "Commercial",
        countryCode = "GB"
    ),
    88 to AgencyInfo(
        name = "China Aerospace Science and Technology Corporation",
        featured = true,
        type = "Government",
        countryCode = "CN"
    ),
    89 to AgencyInfo(
        name = "Fairchild Space and Electronics Division",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    90 to AgencyInfo(
        name = "Fokker Space & Systems",
        featured = false,
        type = "Commercial",
        countryCode = "NL"
    ),
    91 to AgencyInfo(
        name = "General Electric",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    92 to AgencyInfo(
        name = "Hawker Siddeley Dynamics",
        featured = false,
        type = "Commercial",
        countryCode = "GB"
    ),
    93 to AgencyInfo(
        name = "Hughes Aircraft",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    94 to AgencyInfo(
        name = "IHI Corporation",
        featured = false,
        type = "Commercial",
        countryCode = "JP"
    ),
    95 to AgencyInfo(
        name = "Israel Aerospace Industries",
        featured = false,
        type = "Commercial",
        countryCode = "IL"
    ),
    96 to AgencyInfo(
        name = "Khrunichev State Research and Production Space Center",
        featured = true,
        type = "Government",
        countryCode = "RU"
    ),
    97 to AgencyInfo(
        name = "NPO Lavochkin",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    98 to AgencyInfo(
        name = "Mitsubishi Heavy Industries",
        featured = false,
        type = "Commercial",
        countryCode = "JP"
    ),
    99 to AgencyInfo(
        name = "Northrop Grumman Space Technology",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    100 to AgencyInfo(
        name = "Orbital Sciences Corporation",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    101 to AgencyInfo(
        name = "Philco Ford",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    102 to AgencyInfo(
        name = "Rockwell International",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    103 to AgencyInfo(
        name = "RKK Energiya",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    104 to AgencyInfo(
        name = "SPAR Aerospace",
        featured = false,
        type = "Commercial",
        countryCode = "CA"
    ),
    105 to AgencyInfo(
        name = "SpaceDev",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    106 to AgencyInfo(
        name = "General Dynamics",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    107 to AgencyInfo(
        name = "Surrey Satellite Technology Ltd",
        featured = false,
        type = "Commercial",
        countryCode = "GB"
    ),
    108 to AgencyInfo(
        name = "Swales Aerospace",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    109 to AgencyInfo(
        name = "Turkish Aerospace Industries",
        featured = false,
        type = "Commercial",
        countryCode = "TR"
    ),
    110 to AgencyInfo(
        name = "TRW",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    111 to AgencyInfo(
        name = "Progress State Research and Production Rocket Space Center",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    112 to AgencyInfo(
        name = "Yuzhnoye Design Bureau",
        featured = false,
        type = "Commercial",
        countryCode = "UA"
    ),
    113 to AgencyInfo(
        name = "INVAP",
        featured = false,
        type = "Commercial",
        countryCode = "AR"
    ),
    115 to AgencyInfo(
        name = "Arianespace",
        featured = true,
        type = "Commercial",
        countryCode = "FR"
    ),
    116 to AgencyInfo(
        name = "EADS Astrium Space Transportation",
        featured = false,
        type = "Commercial",
        countryCode = "FR"
    ),
    117 to AgencyInfo(
        name = "Eurockot Launch Services",
        featured = false,
        type = "Commercial",
        countryCode = "DE"
    ),
    118 to AgencyInfo(
        name = "International Launch Services | Khrunichev State Research and Production Space Center",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    119 to AgencyInfo(
        name = "ISC Kosmotras",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    120 to AgencyInfo(
        name = "SpaceQuest, Ltd.",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    121 to AgencyInfo(
        name = "SpaceX",
        featured = true,
        type = "Commercial",
        countryCode = "US"
    ),
    122 to AgencyInfo(
        name = "Sea Launch",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    123 to AgencyInfo(
        name = "Starsem SA",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    124 to AgencyInfo(
        name = "United Launch Alliance",
        featured = true,
        type = "Commercial",
        countryCode = "US"
    ),
    125 to AgencyInfo(
        name = "A.M. Makarov Yuzhny Machine-Building Plant",
        featured = false,
        type = "Commercial",
        countryCode = "UA"
    ),
    126 to AgencyInfo(
        name = "Deep Space Industries",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    127 to AgencyInfo(
        name = "Robotics Institute",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    128 to AgencyInfo(
        name = "Planetary Resources",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    129 to AgencyInfo(
        name = "Tethers Unlimited, Inc.",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    130 to AgencyInfo(
        name = "RUAG Space",
        featured = false,
        type = "Commercial",
        countryCode = "CH"
    ),
    131 to AgencyInfo(
        name = "Andrews Space",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    132 to AgencyInfo(
        name = "Kongsberg Defence & Aerospace",
        featured = false,
        type = "Commercial",
        countryCode = "NO"
    ),
    133 to AgencyInfo(
        name = "Aerojet",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    134 to AgencyInfo(
        name = "American Rocket Company",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    135 to AgencyInfo(
        name = "Rocketdyne",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    136 to AgencyInfo(
        name = "Ad Astra Rocket Company",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    137 to AgencyInfo(
        name = "Reaction Engines Ltd.",
        featured = false,
        type = "Commercial",
        countryCode = "GB"
    ),
    138 to AgencyInfo(
        name = "Snecma",
        featured = false,
        type = "Commercial",
        countryCode = "FR"
    ),
    139 to AgencyInfo(
        name = "Armadillo Aerospace",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    140 to AgencyInfo(
        name = "Bigelow Aerospace",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    141 to AgencyInfo(
        name = "Blue Origin",
        featured = true,
        type = "Commercial",
        countryCode = "US"
    ),
    142 to AgencyInfo(
        name = "Copenhagen Suborbitals",
        featured = false,
        type = "Commercial",
        countryCode = "DK"
    ),
    143 to AgencyInfo(
        name = "PlanetSpace",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    144 to AgencyInfo(
        name = "Scaled Composites",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    145 to AgencyInfo(
        name = "XCOR  Aerospace",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    146 to AgencyInfo(
        name = "Canadian Arrow",
        featured = false,
        type = "Commercial",
        countryCode = "CA"
    ),
    147 to AgencyInfo(
        name = "Rocket Lab Ltd",
        featured = true,
        type = "Commercial",
        countryCode = "US"
    ),
    148 to AgencyInfo(
        name = "Scorpius Space Launch Company",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    149 to AgencyInfo(
        name = "Interorbital Systems",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    150 to AgencyInfo(
        name = "Masten Space Systems",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    151 to AgencyInfo(
        name = "Swedish Space Corp",
        featured = false,
        type = "Commercial",
        countryCode = "SE"
    ),
    152 to AgencyInfo(
        name = "UP Aerospace",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    153 to AgencyInfo(
        name = "McDonnell Douglas",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    154 to AgencyInfo(
        name = "Production Corporation Polyot",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    156 to AgencyInfo(
        name = "Alliant Techsystems",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    157 to AgencyInfo(
        name = "Bristol Aerospace Company",
        featured = false,
        type = "Commercial",
        countryCode = "CA"
    ),
    158 to AgencyInfo(
        name = "Chrysler",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    159 to AgencyInfo(
        name = "Avio S.p.A",
        featured = false,
        type = "Commercial",
        countryCode = "IT"
    ),
    160 to AgencyInfo(
        name = "Royal Australian Air Force",
        featured = false,
        type = "Government",
        countryCode = "AU"
    ),
    161 to AgencyInfo(
        name = "United States Air Force",
        featured = false,
        type = "Government",
        countryCode = "US"
    ),
    162 to AgencyInfo(
        name = "People's Liberation Army",
        featured = false,
        type = "Government",
        countryCode = "CN"
    ),
    163 to AgencyInfo(
        name = "Russian Aerospace Defence Forces | ROSCOSMOS",
        featured = false,
        type = "Government",
        countryCode = "RU"
    ),
    165 to AgencyInfo(
        name = "US Army",
        featured = false,
        type = "Government",
        countryCode = "US"
    ),
    166 to AgencyInfo(
        name = "US Navy",
        featured = false,
        type = "Government",
        countryCode = "US"
    ),
    167 to AgencyInfo(
        name = "Space Florida",
        featured = false,
        type = "Government",
        countryCode = "US"
    ),
    175 to AgencyInfo(
        name = "Ministry of Defence of the Russian Federation | ROSCOSMOS",
        featured = false,
        type = "Government",
        countryCode = "RU"
    ),
    177 to AgencyInfo(
        name = "China Great Wall Industry Corporation",
        featured = false,
        type = "Commercial",
        countryCode = "CN"
    ),
    178 to AgencyInfo(
        name = "Airbus Defence and Space",
        featured = false,
        type = "Multinational",
        countryCode = "EU"
    ),
    179 to AgencyInfo(
        name = "Orbital ATK",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    181 to AgencyInfo(
        name = "National Reconnaissance Office",
        featured = false,
        type = "Government",
        countryCode = "US"
    ),
    182 to AgencyInfo(
        name = "National Space Agency of the Republic of Kazakhstan",
        featured = false,
        type = "Government",
        countryCode = "KZ"
    ),
    184 to AgencyInfo(
        name = "China Aerospace Science and Industry Corporation",
        featured = false,
        type = "Government",
        countryCode = "CN"
    ),
    186 to AgencyInfo(
        name = "Polish Space Agency",
        featured = false,
        type = "Government",
        countryCode = "PL"
    ),
    187 to AgencyInfo(
        name = "GK Launch Services JV",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    188 to AgencyInfo(
        name = "Gilmour Space Technologies",
        featured = false,
        type = "Private",
        countryCode = "AU"
    ),
    189 to AgencyInfo(
        name = "China Aerospace Corporation",
        featured = false,
        type = "Government",
        countryCode = "CN"
    ),
    190 to AgencyInfo(
        name = "Antrix Corporation Limited",
        featured = false,
        type = "Commercial",
        countryCode = "IN"
    ),
    191 to AgencyInfo(
        name = "United Space Alliance",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    192 to AgencyInfo(
        name = "Lockheed Space Operations Company",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    193 to AgencyInfo(
        name = "Russian Space Forces | ROSCOSMOS",
        featured = false,
        type = "Government",
        countryCode = "RU"
    ),
    194 to AgencyInfo(
        name = "ExPace",
        featured = false,
        type = "Commercial",
        countryCode = "CN"
    ),
    195 to AgencyInfo(
        name = "Sandia National Laboratories",
        featured = false,
        type = "Government",
        countryCode = "US"
    ),
    196 to AgencyInfo(
        name = "Land Launch",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    197 to AgencyInfo(
        name = "Lockheed Martin Space Operations",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    198 to AgencyInfo(
        name = "Mohammed bin Rashid Space Centre",
        featured = false,
        type = "Government",
        countryCode = "AE"
    ),
    199 to AgencyInfo(
        name = "Virgin Orbit",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    201 to AgencyInfo(
        name = "Vector",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    202 to AgencyInfo(
        name = "Iridium Communications",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    203 to AgencyInfo(
        name = "SES",
        featured = false,
        type = "Commercial",
        countryCode = "LU"
    ),
    204 to AgencyInfo(
        name = "Globalstar",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    205 to AgencyInfo(
        name = "Inmarsat",
        featured = false,
        type = "Commercial",
        countryCode = "GB"
    ),
    206 to AgencyInfo(
        name = "Intelsat",
        featured = false,
        type = "Commercial",
        countryCode = "LU"
    ),
    207 to AgencyInfo(
        name = "Arab Satellite Communications Organization",
        featured = false,
        type = "Commercial",
        countryCode = "SA"
    ),
    208 to AgencyInfo(
        name = "Telesat",
        featured = false,
        type = "Commercial",
        countryCode = "CA"
    ),
    210 to AgencyInfo(
        name = "National Oceanic and Atmospheric Administration",
        featured = false,
        type = "Government",
        countryCode = "US",
        abbrev = "NOAA"
    ),
    211 to AgencyInfo(
        name = "National Security Agency",
        featured = false,
        type = "Government",
        countryCode = "US",
        abbrev = "NSA"
    ),
    212 to AgencyInfo(
        name = "Digital Globe",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    213 to AgencyInfo(
        name = "Missile Defense Agency",
        featured = false,
        type = "Government",
        countryCode = "US"
    ),
    214 to AgencyInfo(
        name = "Spire Global",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    224 to AgencyInfo(
        name = "GeoOptics",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    225 to AgencyInfo(
        name = "1worldspace",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    226 to AgencyInfo(
        name = "PanAmSat",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    227 to AgencyInfo(
        name = "UK Ministry Of Defence",
        featured = false,
        type = "Government",
        countryCode = "GB"
    ),
    228 to AgencyInfo(
        name = "National Space Development Agency of Japan",
        featured = false,
        type = "Government",
        countryCode = "JP"
    ),
    229 to AgencyInfo(
        name = "Eutelsat",
        featured = false,
        type = "Commercial",
        countryCode = "FR"
    ),
    230 to AgencyInfo(
        name = "Broadcasting Satellite System Corporation",
        featured = false,
        type = "Commercial",
        countryCode = "JP"
    ),
    231 to AgencyInfo(
        name = "SKY Perfect JSAT Group",
        featured = false,
        type = "Commercial",
        countryCode = "JP"
    ),
    232 to AgencyInfo(
        name = "European Organisation for the Exploitation of Meteorological Satellites",
        featured = false,
        type = "Multinational",
        countryCode = "EU"
    ),
    233 to AgencyInfo(
        name = "Direction générale de l'armement",
        featured = false,
        type = "Government",
        countryCode = "FR"
    ),
    234 to AgencyInfo(
        name = "XTAR LLC",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    235 to AgencyInfo(
        name = "Thaicom",
        featured = false,
        type = "Commercial",
        countryCode = "TH"
    ),
    236 to AgencyInfo(
        name = "DirecTV",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    237 to AgencyInfo(
        name = "PT Telkom",
        featured = false,
        type = "Commercial",
        countryCode = "ID"
    ),
    238 to AgencyInfo(
        name = "Hisdesat",
        featured = false,
        type = "Commercial",
        countryCode = "ES"
    ),
    239 to AgencyInfo(
        name = "Satmex",
        featured = false,
        type = "Commercial",
        countryCode = "MX"
    ),
    240 to AgencyInfo(
        name = "Optus",
        featured = false,
        type = "Commercial",
        countryCode = "AU"
    ),
    241 to AgencyInfo(
        name = "WildBlue",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    242 to AgencyInfo(
        name = "Paradigm Secure Communications",
        featured = false,
        type = "Commercial",
        countryCode = "GB"
    ),
    243 to AgencyInfo(
        name = "Hughes",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    244 to AgencyInfo(
        name = "Star One",
        featured = false,
        type = "Commercial",
        countryCode = "BR"
    ),
    245 to AgencyInfo(
        name = "Regional African Satellite Communication Organization",
        featured = false,
        type = "Commercial",
        countryCode = "MU"
    ),
    246 to AgencyInfo(
        name = "Vietnam Posts and Telecommunications Group",
        featured = false,
        type = "Commercial",
        countryCode = "VN"
    ),
    247 to AgencyInfo(
        name = "Türksat",
        featured = false,
        type = "Commercial",
        countryCode = "TR"
    ),
    248 to AgencyInfo(
        name = "ProtoStar",
        featured = false,
        type = "Commercial",
        countryCode = "BM"
    ),
    249 to AgencyInfo(
        name = "Echostar",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    250 to AgencyInfo(
        name = "HispaSat",
        featured = false,
        type = "Commercial",
        countryCode = "ES"
    ),
    251 to AgencyInfo(
        name = "AlYahSat",
        featured = false,
        type = "Commercial",
        countryCode = "AE"
    ),
    252 to AgencyInfo(
        name = "MEASAT Satellite Systems",
        featured = false,
        type = "Commercial",
        countryCode = "MY"
    ),
    253 to AgencyInfo(
        name = "French Armed Forces",
        featured = false,
        type = "Government",
        countryCode = "FR"
    ),
    254 to AgencyInfo(
        name = "British Satellite Broadcasting",
        featured = false,
        type = "Commercial",
        countryCode = "GB"
    ),
    255 to AgencyInfo(
        name = "Avanti Communications",
        featured = false,
        type = "Commercial",
        countryCode = "GB"
    ),
    256 to AgencyInfo(
        name = "Mexican Satellite System",
        featured = false,
        type = "Government",
        countryCode = "MX"
    ),
    257 to AgencyInfo(
        name = "Northrop Grumman Innovation Systems",
        featured = true,
        type = "Commercial",
        countryCode = "US"
    ),
    259 to AgencyInfo(
        name = "LandSpace",
        featured = false,
        countryCode = "CN"
    ),
    260 to AgencyInfo(
        name = "Planet Labs",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    261 to AgencyInfo(
        name = "Korean Astronaut Program",
        featured = false,
        type = "Government",
        countryCode = "KR"
    ),
    263 to AgencyInfo(
        name = "OneSpace",
        featured = false,
        countryCode = "CN"
    ),
    265 to AgencyInfo(
        name = "Firefly Aerospace",
        featured = false,
        countryCode = "US"
    ),
    266 to AgencyInfo(
        name = "Relativity Space",
        featured = false,
        countryCode = "US"
    ),
    270 to AgencyInfo(
        name = "Strategic Missile Troops",
        featured = false,
        countryCode = "RU"
    ),
    271 to AgencyInfo(
        name = "Army Ballistic Missile Agency",
        featured = false,
        countryCode = "US"
    ),
    272 to AgencyInfo(
        name = "Chinarocket Co., Ltd.",
        featured = false,
        countryCode = "CN"
    ),
    274 to AgencyInfo(
        name = "iSpace",
        featured = false,
        countryCode = "CN"
    ),
    282 to AgencyInfo(
        name = "Aevum",
        featured = false,
        countryCode = "US"
    ),
    285 to AgencyInfo(
        name = "Astra Space",
        featured = false,
        countryCode = "US"
    ),
    999 to AgencyInfo(
        name = "North American Aviation | Boeing",
        featured = false,
        countryCode = "US"
    ),
    1000 to AgencyInfo(
        name = "Energia",
        featured = false,
        type = "Government",
        countryCode = "RU"
    ),
    1001 to AgencyInfo(
        name = "EXOS Aerospace",
        featured = false,
        countryCode = "US"
    ),
    1002 to AgencyInfo(
        name = "Interstellar Technologies",
        featured = false,
        countryCode = "JP"
    ),
    1003 to AgencyInfo(
        name = "Aérospatiale",
        featured = false,
        type = "Government",
        countryCode = "FR"
    ),
    1004 to AgencyInfo(
        name = "Convair",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    1005 to AgencyInfo(
        name = "Royal Aircraft Establishment",
        featured = false,
        type = "Government",
        countryCode = "GB"
    ),
    1006 to AgencyInfo(
        name = "Vought",
        featured = false,
        countryCode = "US"
    ),
    1007 to AgencyInfo(
        name = "Space Services Inc.",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    1008 to AgencyInfo(
        name = "Société d'étude et de réalisation d'engins balistiques",
        featured = false,
        type = "Commercial",
        countryCode = "FR"
    ),
    1009 to AgencyInfo(
        name = "Institute of Space and Astronautical Science",
        featured = false,
        type = "Government",
        countryCode = "JP"
    ),
    1010 to AgencyInfo(
        name = "KB Mashinostroyeniya",
        featured = false,
        type = "Government",
        countryCode = "RU"
    ),
    1011 to AgencyInfo(
        name = "Moscow Institute of Thermal Technology | ROSCOSMOS",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    1012 to AgencyInfo(
        name = "Department of Aerospace Science and Technology",
        featured = false,
        type = "Government",
        countryCode = "BR"
    ),
    1013 to AgencyInfo(
        name = "Makeyev Rocket Design Bureau",
        featured = false,
        type = "Commercial",
        countryCode = "RU"
    ),
    1014 to AgencyInfo(
        name = "Martin Marietta",
        featured = false,
        type = "Commercial",
        countryCode = "US"
    ),
    1015 to AgencyInfo(
        name = "European Launcher Development Organisation",
        featured = false,
        type = "Government",
        countryCode = "EU"
    ),
    1016 to AgencyInfo(
        name = "Weapons Research Establishment",
        featured = false,
        type = "Government",
        countryCode = "AU"
    ),
    1017 to AgencyInfo(
        name = "Science and Engineering Research Council",
        featured = false,
        type = "Government",
        countryCode = "GB"
    ),
    1018 to AgencyInfo(
        name = "National Research Council",
        featured = false,
        type = "Government",
        countryCode = "IT"
    ),
    1019 to AgencyInfo(
        name = "European Space Research Organisation",
        featured = false,
        type = "Multinational",
        countryCode = "EU"
    )
)