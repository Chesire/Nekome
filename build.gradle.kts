import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    alias(libs.plugins.ktlint)

    alias(libs.plugins.aboutlibraries) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.androidx.navigation.safeargs) apply false
    alias(libs.plugins.google.dagger.hilt.android) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.kotlin.android) apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        verbose = true
        android = true
        outputToConsole = true
        disabledRules = listOf("max-line-length")

        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        }
    }
    afterEvaluate {
        if (plugins.hasPlugin("android") || plugins.hasPlugin("android-library")) {
            configure<com.android.build.gradle.BaseExtension> {
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }
            configure<org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension> {
                jvmToolchain(17)
            }
        }
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

tasks.register<Detekt>("detektCheck") {
    parallel = true
    setSource(files(projectDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    buildUponDefaultConfig = false
}

koverMerged {
    enable()

    filters {
        classes {
            excludes.addAll(
                listOf(
                    "*.databinding.*", // Autogen ViewBinding
                    "dagger.*", // Autogen Dagger code
                    "hilt_*", // Autogen Hilt code
                    "*BuildConfig", // Autogen Build file
                    "*ComposableSingletons\$*", // Autogen Compose code
                    "*JsonAdapter", // Autogen Moshi code
                    "*Directions\$*", // Autogen Navigation
                    "*_AssistedFactory_Impl", // Autogen Dagger code
                    "*_Factory", // Autogen Dagger code
                    "*_Factory\$*", // Autogen Dagger code
                    "*_HiltModules*", // Autogen Dagger code
                    "*_MembersInjector", // Autogen Dagger code

                    "*Activity", // Activities
                    "*Activity\$*", // Activities
                    "*Fragment", // Fragments
                    "*Fragment\$*", // Fragments
                    "*ScreenKt*", // Compose screens
                    "*ScreenKt*\$*", // Compose screens
                    "com.chesire.nekome.injection.*" // Dagger injection code
                )
            )
        }
        annotations {
            excludes.addAll(
                listOf(
                    "androidx.compose.runtime.Composable" // Compose components
                )
            )
        }
        projects {
            excludes.addAll(
                listOf(
                    ":core:compose", // View code
                    ":testing" // Testing code
                )
            )
        }
    }
}

dependencies {
    detektPlugins(libs.detekt.compose)
    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt.kotlin.compiler)
}
