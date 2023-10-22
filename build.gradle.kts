plugins {
    id("java")
    kotlin("jvm") version "1.9.20-RC"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1")) // дефолт
    testImplementation("org.junit.jupiter:junit-jupiter") // дефолт
    implementation("com.google.code.gson:gson:2.10.1") // обработка json
    implementation("com.googlecode.json-simple:json-simple:1.1.1") // обработка json
    implementation("com.toedter:jcalendar:1.4") // добавление календаря для выбора дат
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}