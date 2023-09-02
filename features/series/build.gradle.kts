plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.chesire.nekome.app.series"
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
    implementation(project(":core:preferences"))
    implementation(project(":core:resources"))
    implementation(project(":libraries:core"))
    implementation(project(":libraries:datasource:series"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material) // This is for the pull to refresh
    implementation(libs.androidx.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.bundles.compose)
    implementation(libs.coil)
    implementation(libs.google.accompanist.flowlayout)
    implementation(libs.google.hilt.android)
    implementation(libs.kotlin.result)
    implementation(libs.squareup.moshi)
    implementation(libs.timber)
    debugImplementation(libs.androidx.compose.ui.tooling)
    ksp(libs.google.hilt.android.compiler)

    testImplementation(project(":testing"))
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockk)
}
