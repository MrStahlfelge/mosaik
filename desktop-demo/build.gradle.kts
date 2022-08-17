plugins {
    id("kotlin")
    id("org.jetbrains.compose") version "1.1.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(project(":common-compose"))
    implementation(project(":serialization-gson"))
    implementation(project(":clientconnector-okhttp"))

    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)

    // Qr Code generation
    arrayOf("core","kotlin").forEach()
    { implementation("org.boofcv:boofcv-$it:0.40.1") {
        exclude("org.boofcv", "boofcv-swing")
    } }
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
