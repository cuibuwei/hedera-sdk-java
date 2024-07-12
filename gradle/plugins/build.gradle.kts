plugins {
    `kotlin-dsl`
}

repositories.gradlePluginPortal()

dependencies {
    implementation("com.autonomousapps:dependency-analysis-gradle-plugin:1.32.0")
    implementation("com.github.spotbugs.snom:spotbugs-gradle-plugin:6.0.12")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
    implementation("io.github.gradle-nexus:publish-plugin:1.3.0")
    implementation("net.ltgt.gradle:gradle-errorprone-plugin:3.1.0")
    implementation("org.gradlex:extra-java-module-info:1.8")
    implementation("org.gradlex:java-module-dependencies:1.6.5")
    implementation("org.gradlex:jvm-dependency-conflict-resolution:2.1")
    implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:4.4.1.3373")

    implementation("org.gradle.toolchains:foojay-resolver:0.8.0")
    implementation("com.gradle:develocity-gradle-plugin:3.17.2")
}