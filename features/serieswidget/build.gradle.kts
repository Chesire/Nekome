plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.chesire.nekome.feature.serieswidget"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":core:compose"))

    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    implementation(libs.bundles.compose)
    implementation(libs.google.hilt.android)
    implementation(libs.kotlin.result)
    implementation(libs.timber)
    debugImplementation(libs.androidx.compose.ui.tooling)
    ksp(libs.google.hilt.android.compiler)
}
