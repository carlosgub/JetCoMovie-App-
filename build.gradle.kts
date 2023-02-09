import io.gitlab.arturbosch.detekt.Detekt

buildscript {
    dependencies {
        //Klint Twitter Rules
        classpath("com.twitter.compose.rules:ktlint:0.0.26")
    }
}

plugins {
    id("com.android.application") version "7.3.1" apply false
    id("com.android.library") version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
    id("com.google.dagger.hilt.android") version "2.43.2" apply false
    id("org.jmailen.kotlinter") version "3.13.0" apply false
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
}

//region Detekt

dependencies {
    detektPlugins("com.twitter.compose.rules:detekt:0.0.26")
}

detekt {
    buildUponDefaultConfig = true
    allRules = false // activate all available (even unstable) rules.
    config = files("$rootDir/detekt/config.yml")
    baseline = file("$rootDir/detekt/baseline.xml")
}

tasks.withType<Detekt>().configureEach {
    setSource(file(projectDir))
    include("**/*.kt")
    exclude("**/build/**")
    autoCorrect = true
    reports {
        html.required.set(true)
        sarif.required.set(false)
        xml.required.set(false)
        md.required.set(false)
        txt.required.set(false)
    }
}
//endregion
