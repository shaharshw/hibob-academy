import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-jersey")
	implementation("org.springframework.boot:spring-boot-starter-logging")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.google.guava:guava:33.3.0-jre")
	implementation("org.apache.commons:commons-lang3:3.16.0")
	implementation("org.apache.httpcomponents:httpclient:4.5.14")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jooq:jooq:3.16.9")
	implementation("org.slf4j:slf4j-api")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	implementation("org.postgresql:postgresql")

	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.junit.jupiter:junit-jupiter-params")
	testImplementation("org.hamcrest:hamcrest-library")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}


tasks {
	withType<Test> {
		useJUnitPlatform()
		testLogging {
			displayGranularity = 1
			showCauses = true
			showStackTraces = true
			exceptionFormat = TestExceptionFormat.FULL
			events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
		}
		reports.junitXml
		val dbUrl = System.getenv("DB_URL")
		if (dbUrl != null && dbUrl.isNotBlank()) {
			systemProperty("DB_URL", dbUrl)
		} else {
			systemProperty("DB_URL", "jdbc:postgresql://localhost:5432/email_service?user=bob&password=dev")
		}
		systemProperty("spring.profiles.active", "development,test")
	}
}
