import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val publishingToken: String? = System.getenv("PUBLISH_TOKEN")

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "co.anbora.labs"
version = "2024.3.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
    implementation("com.github.alexdlaird:java-ngrok:2.2.7")
    implementation("com.nfeld.jsonpathkt:jsonpathkt:2.0.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.jayway.jsonpath:json-path:2.8.0")
}

apply {
    plugin("kotlin")
    plugin("org.jetbrains.intellij")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("LATEST-EAP-SNAPSHOT")
    type.set("PY")

    plugins.set(listOf())
}

tasks {

    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("241.*")
        changeNotes.set(file("src/main/html/change-notes.html").inputStream().readBytes().toString(Charsets.UTF_8))
        pluginDescription.set(file("src/main/html/description.html").inputStream().readBytes().toString(Charsets.UTF_8))
    }

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }

    publishPlugin {
        token.set(publishingToken)
    }
}
