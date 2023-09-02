plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.chesire.nekome.core.preferences"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(project(":core:resources"))
    implementation(project(":libraries:core"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.google.hilt.android)
    implementation(libs.squareup.moshi)
    ksp(libs.google.hilt.android.compiler)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
}
