plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.chesire.nekome.core"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":core:resources"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core)
    implementation(libs.androidx.room.runtime)
    implementation(libs.google.hilt.android)
    implementation(libs.google.material)
    implementation(libs.squareup.moshi)
    ksp(libs.google.hilt.android.compiler)
    ksp(libs.squareup.moshi.codegen)

    testImplementation(project(":testing"))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
