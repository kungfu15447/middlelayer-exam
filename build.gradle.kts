import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	id("org.springframework.boot") version "2.6.3"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.jpa") version "1.6.10"
}

group = "com.middlelayer"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.core:jackson-core:2.13.1")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
	implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.13.1")
	implementation("commons-io:commons-io:2.11.0")
	implementation("org.apache.commons:commons-lang3:3.12.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.squareup.okhttp3:okhttp:4.9.3")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("com.ninja-squad:springmockk:3.1.0")
}

//This layering should be done on default by spring boot
//But this is to show how it makes layered jars and in which order
tasks.getByName<BootJar>("bootJar") {
	layered {
		isEnabled = true
		application {
			intoLayer("spring-boot-loader") {
				include("org/springframework/boot/loader/**")
			}
			intoLayer("application")
		}
		dependencies {
			intoLayer("snapshot-dependencies") {
				include("*:*:*SNAPSHOT")
			}
			intoLayer("dependencies")
		}
		layerOrder = listOf("dependencies", "spring-boot-loader", "snapshot-dependencies", "application")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.getByName<BootBuildImage>("bootBuildImage") {
	imageName = "kungfu15447/middlelayer-repo"
}

tasks.withType<Test> {
	useJUnitPlatform()
}
