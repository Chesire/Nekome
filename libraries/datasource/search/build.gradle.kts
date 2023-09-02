plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.chesire.nekome.datasource.search"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":libraries:core"))

    implementation(libs.kotlin.result)
}
