plugins {
    id("kotlin")
    id("org.jetbrains.compose") version "1.1.0"
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

dependencies {
    api(project(":common-runtime"))
    api("org.jetbrains:markdown-jvm:0.3.1")

    api(compose.runtime)
    api(compose.foundation)
    api(compose.material)
    implementation(compose.materialIconsExtended)
    // Needed only for preview.
    implementation(compose.preview)
}
