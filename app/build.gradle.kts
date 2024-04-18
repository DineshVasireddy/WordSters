plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    id("application")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit test framework.
    testImplementation("junit:junit:4.13")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:30.1-jre")
}

// Define the main class for the application.
application {
    // Change the main class to the one in the client directory.
    mainClass.set("Server.Server")
}

// Define source sets to include the new directory structure.
sourceSets {
    main {
        java {
            srcDirs("src")
            // Exclude the test directory
            exclude("test")
        }
    }
}

tasks.register<JavaExec>("runServer") {
    mainClass = "Server.Server"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runClient") {
    mainClass = "Client.Main"
    classpath = sourceSets["main"].runtimeClasspath
}