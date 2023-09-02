plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.chesire.nekome.testing"
    compileSdk = libs.versions.sdk.get().toInt()

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }
    packaging {
        resources {
            excludes += listOf("META-INF/AL2.0", "META-INF/LGPL2.1", "META-INF/*.kotlin_module")
        }
    }
}

dependencies {
    implementation(project(":libraries:core"))
    implementation(project(":libraries:database"))
    implementation(project(":libraries:datasource:series"))
    implementation(project(":libraries:datasource:user"))

    implementation(libs.junit)
    implementation(libs.kotlin.coroutines.test)
}
