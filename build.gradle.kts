import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val publishingToken: String? = System.getenv("PUBLISH_TOKEN")

plugins {
    id("java")
    kotlin("jvm") version "1.6.21"
    id("org.jetbrains.intellij") version "1.8.0"
}

group = "co.anbora.labs"
version = "1.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
    implementation("com.github.alexdlaird:java-ngrok:1.5.6")
}

apply {
    plugin("kotlin")
    plugin("org.jetbrains.intellij")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2022.2")
    plugins.set(listOf())
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {

    patchPluginXml {
        sinceBuild.set("213.7172")
        changeNotes.set(file("src/main/html/change-notes.html").inputStream().readBytes().toString(Charsets.UTF_8))
        pluginDescription.set(file("src/main/html/description.html").inputStream().readBytes().toString(Charsets.UTF_8))
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            languageVersion = "1.6"
            apiVersion = "1.6"
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }

    publishPlugin {
        token.set(publishingToken)
    }
}
