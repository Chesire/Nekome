plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.chesire.nekome.datasource.activity"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":libraries:core"))

    implementation(libs.google.hilt.android)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.result)
}
