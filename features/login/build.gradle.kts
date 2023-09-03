plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
}

android {
    namespace = "com.chesire.nekome.app.login"
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
    implementation(project(":core:resources"))
    implementation(project(":libraries:core"))
    implementation(project(":libraries:datasource:auth"))
    implementation(project(":libraries:datasource:series"))
    implementation(project(":libraries:datasource:user"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.bundles.compose)
    implementation(libs.google.hilt.android)
    implementation(libs.google.material)
    implementation(libs.kotlin.result)
    implementation(libs.timber)
    debugImplementation(libs.androidx.compose.ui.tooling)
    ksp(libs.google.hilt.android.compiler)

    testImplementation(project(":testing"))
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockk)
}
