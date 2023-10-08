plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.androidx.navigation.safeargs)
    alias(libs.plugins.aboutlibraries)
}

android {
    namespace = "com.chesire.nekome"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        applicationId = "com.chesire.nekome"
        minSdk = 21
        targetSdk = libs.versions.sdk.get().toInt()
        versionCode = 23100818 // Date of build formatted as 'yyMMddHH'
        versionName = "2.0.5"
        testInstrumentationRunner = "com.chesire.nekome.TestRunner"
        resourceConfigurations += listOf("en", "ja")
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
        }
        named("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
        buildTypes.forEach {
            it.resValue("string", "version", defaultConfig.versionName!!)
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    testOptions {
        animationsDisabled = true
    }
    packaging {
        resources.excludes.add("META-INF/*")
        resources.excludes.add("MANIFEST.MF")
    }
    lint {
        abortOnError = false
        checkAllWarnings = true
        checkDependencies = true
    }
}

dependencies {
    implementation(project(":core:compose"))
    implementation(project(":core:preferences"))
    implementation(project(":core:resources"))
    implementation(project(":features:login"))
    implementation(project(":features:search"))
    implementation(project(":features:series"))
    implementation(project(":features:serieswidget"))
    implementation(project(":features:settings"))
    implementation(project(":libraries:core"))
    implementation(project(":libraries:database"))
    implementation(project(":libraries:datasource:activity"))
    implementation(project(":libraries:datasource:auth"))
    implementation(project(":libraries:datasource:search"))
    implementation(project(":libraries:datasource:series"))
    implementation(project(":libraries:datasource:trending"))
    implementation(project(":libraries:datasource:user"))
    implementation(project(":libraries:kitsu"))
    implementation(project(":libraries:kitsu:activity"))
    implementation(project(":libraries:kitsu:auth"))
    implementation(project(":libraries:kitsu:library"))
    implementation(project(":libraries:kitsu:search"))
    implementation(project(":libraries:kitsu:trending"))
    implementation(project(":libraries:kitsu:user"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.work.runtime)
    implementation(libs.bundles.compose)
    implementation(libs.coil)
    implementation(libs.google.hilt.android)
    implementation(libs.google.material)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.result)
    implementation(libs.lifecyklelog)
    implementation(libs.squareup.moshi)
    implementation(libs.squareup.okhttp3.logging.interceptor)
    implementation(libs.squareup.retrofit2.converter.moshi)
    implementation(libs.squareup.retrofit2)
    implementation(libs.timber)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.squareup.leakcanary)
    ksp(libs.androidx.hilt.compiler)
    ksp(libs.google.hilt.android.compiler)

    testImplementation(project(":testing"))
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockk)

    androidTestImplementation(project(":testing"))
    androidTestImplementation(libs.adevinta.barista)
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.espresso.intents)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.google.hilt.android.testing)
    androidTestImplementation(libs.mockk.android)
    kspAndroidTest(libs.google.hilt.android.compiler)
}
