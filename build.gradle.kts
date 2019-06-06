import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "no.nav.plattform"
version = "1.0.0"

val micrometerVersion = "1.1.4"
val ktorVersion = "1.2.0"
val logbackVersion = "1.2.3"
val logstashLogbackEncoderVersion = "6.0"

tasks.withType<Jar> {
    manifest.attributes["Main-Class"] = "no.nav.plattform.BootstrapKt"
}


plugins {
    java
    kotlin("jvm") version "1.3.21"
    id("org.jmailen.kotlinter") version "1.21.0"
    id("com.diffplug.gradle.spotless") version "3.18.0"
    id("com.github.johnrengelman.shadow") version "5.0.0"
}



repositories {
    mavenCentral()
    jcenter()
}


dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-metrics-micrometer:$ktorVersion")
    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerVersion")

    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("net.logstash.logback:logstash-logback-encoder:$logstashLogbackEncoderVersion")
}


tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
