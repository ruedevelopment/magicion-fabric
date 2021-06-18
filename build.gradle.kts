import org.gradle.api.tasks.compile.JavaCompile as CompileJava
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile as CompileKotlin

plugins {
    java
    kotlin("jvm") version "1.5.10"
    id("fabric-loom") version "0.8-SNAPSHOT"
    `maven-publish`
}

group = project.property("maven_group")!!
base.archivesName.set(project.property("archives_base_name").toString())
version = project.property("mod_version")!!

repositories {
    maven("https://maven.terraformersmc.com/releases")
}

dependencies {
    // PSA: Some older mods, compiled on Loom 0.2.1, might have outdated Maven POMs.
    // You may need to force-disable transitiveness on them.

    fun version(dependency: String) = project.property("${dependency}_version")!!.toString()

    // the basics
    minecraft("com.mojang:minecraft:${version("minecraft")}")
    mappings("net.fabricmc:yarn:${version("yarn")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${version("loader")}")

    // api
    modImplementation("net.fabricmc.fabric-api:fabric-api:${version("fabric_api")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${version("fabric_kotlin")}")

    // runtime util
    modRuntime(group = "com.terraformersmc", name = "modmenu", version = version("mod_menu"))
}

tasks {
    withType<ProcessResources>().configureEach {
        /*
         * DO NOT USE INTELLIJ FOR BUILDING!!!
         * It may be faster, but it will break the mod, as the IntelliJ compiler does not support this task.
         */
        val versions = listOf("mod", "minecraft", "loader", "fabric_api", "fabric_kotlin")
            .associate { key -> key + "_version" to project.property(key + "_version") }

        inputs.properties(versions)

        filesMatching("fabric.mod.json") {
            expand(versions)
        }
    }

    withType<CompileKotlin>().configureEach {
        kotlinOptions {
            jvmTarget = "16"
            languageVersion = "1.5"
        }
    }

    withType<CompileJava>().configureEach {
        // this fixes some edge cases with special characters not displaying correctly
        // if Javadoc is generated, this must be specified in that task too.
        options.encoding = "UTF-8"

        // now need java 16
        options.release.set(16)
    }

    jar {
        from("COPYING") {
            rename { "${it}_${project.base.archivesName}" }
        }

        from("COPYING.LESSER") {
            rename { "${it}_${project.base.archivesName}" }
        }
    }
}

java {
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

// i have no idea if this works
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(tasks.remapJar) {
                builtBy(tasks.remapJar)
            }

            artifact(tasks.kotlinSourcesJar) {
                builtBy(tasks.remapSourcesJar)
            }
        }
    }
}
