plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "music-ms"
include("api-gateway-service")
include("user-service")
include("song-service")
include("artist-service")
include("playlist-service")
