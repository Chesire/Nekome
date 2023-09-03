plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.chesire.nekome.datasource.auth"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":core:preferences"))
    implementation(project(":libraries:core"))

    implementation(libs.androidx.core)
    implementation(libs.androidx.security.crypto)
    implementation(libs.google.hilt.android)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.result)
    implementation(libs.squareup.retrofit2)
    implementation(libs.timber)

    testImplementation(project(":testing"))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
