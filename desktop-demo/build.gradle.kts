plugins {
    id("kotlin")
    id("org.jetbrains.compose") version "1.1.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(project(":common-compose"))
    implementation(project(":serialization-gson"))

    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
}

compose.desktop {
    application {
        mainClass = "org.ergoplatform.mosaik.MainKt"
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )
            packageName = "Ergo Mosaik"
            packageVersion = "1.0.0"
        }
    }
}
