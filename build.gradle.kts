import com.android.build.gradle.BaseExtension
import io.gitlab.arturbosch.detekt.Detekt
import kotlinx.kover.gradle.plugin.dsl.KoverReportExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    alias(libs.plugins.ktlint)

    alias(libs.plugins.aboutlibraries) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.dagger.hilt.android) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.kotlin.android) apply false
}

subprojects {
    afterEvaluate {
        if (plugins.hasPlugin("android") || plugins.hasPlugin("android-library")) {
            apply(plugin = "org.jetbrains.kotlinx.kover")
            apply(plugin = "org.jlleitschuh.gradle.ktlint")

            configure<BaseExtension> {
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
                testOptions {
                    unitTests {
                        all {
                            it.ignoreFailures = true
                        }
                    }
                }
            }
            configure<KotlinAndroidProjectExtension> {
                jvmToolchain(17)
            }
            configure<KoverReportExtension> {
                defaults {
                    mergeWith("debug")
                }
            }
            configure<KtlintExtension> {
                verbose = true
                android = true
                outputToConsole = true
                disabledRules = listOf("max-line-length")

                reporters {
                    reporter(ReporterType.PLAIN)
                }
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

private val excludeProjects = setOf(
    ":core", // Container project
    ":core:compose", // UI Logic
    ":features", // Container project
    ":libraries", // Container project
    ":libraries:datasource", // Container project
    ":testing" // Test code
)
dependencies {
    detektPlugins(libs.detekt.compose)
    detektPlugins(libs.detekt.formatting)
    detektPlugins(libs.detekt.kotlin.compiler)
    subprojects.forEach { subproject ->
        if (!excludeProjects.contains(subproject.path)) {
            kover(subproject)
        }
    }
}

koverReport {
    filters {
        excludes {
            classes(
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
            annotatedBy("androidx.compose.runtime.Composable")
        }
    }
}
