import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import java.lang.System.getenv

plugins {
    id("java")
    id("maven-publish")
    id("com.palantir.git-version") version "3.1.0"
}

val versionDetails: Closure<VersionDetails> by extra

project.group = project.parent?.group!!
project.version = project.parent?.version!!

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

dependencies {

    /* HikariCP */
    implementation("com.zaxxer:HikariCP:${project.property("hikaricp.version")}")

    /* JetBrains Annotations */
    compileOnly("org.jetbrains:annotations:${project.parent?.property("jetbrains.annotations.version")}")
    annotationProcessor("org.jetbrains:annotations:${project.parent?.property("jetbrains.annotations.version")}")

    /* JUnit */
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter:${project.parent?.property("junit.version")}")

}

publishing {

    publications {
        create<MavenPublication>("publish") {
            groupId = project.group as String
            artifactId = project.name
            version = project.version as String
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://repo.mrstudios.pl/public/")
            credentials {
                username = getenv("REPOSITORY_USER")
                password = getenv("REPOSITORY_PASSWORD")
            }
        }
    }

}

tasks {

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    test {
        useJUnitPlatform()
        testLogging {
            events("passed")
        }
    }

    build {
        dependsOn(test)
        if (versionDetails().branchName == "ver/latest")
            finalizedBy(publish)
    }

}