import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    id("java")
    id("com.gradleup.shadow")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

dependencies {
    implementation(project(":oraxen-core"))
    implementation("net.minestom:minestom:2025.12.20-1.21.11")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("net.kyori:adventure-text-minimessage:4.18.0")
    implementation("net.kyori:adventure-nbt:4.18.0")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("oraxen-minestom-${project.rootProject.version}.jar")
        mergeServiceFiles()
        manifest {
            attributes(
                "Main-Class" to "io.th0rgal.oraxen.minestom.OraxenMinestomServer"
            )
        }
    }

    build {
        dependsOn(shadowJar)
    }
}