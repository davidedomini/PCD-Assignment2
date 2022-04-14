plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("io.vertx:vertx-core:4.2.6")
    implementation("io.vertx:vertx-web:4.2.6")

    implementation("io.vertx:vertx-web-client:4.2.6")

    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.24.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}