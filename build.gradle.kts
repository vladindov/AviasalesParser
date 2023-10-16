plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1")) // ������
    testImplementation("org.junit.jupiter:junit-jupiter") // ������
    implementation("com.google.code.gson:gson:2.10.1") // ��������� json
    implementation("com.googlecode.json-simple:json-simple:1.1.1") // ��������� json
    implementation("com.toedter:jcalendar:1.4") // ���������� ��������� ��� ������ ���
}

tasks.test {
    useJUnitPlatform()
}