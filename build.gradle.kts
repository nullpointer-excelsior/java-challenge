import com.adarshr.gradle.testlogger.theme.ThemeType

plugins {
	java
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
	id("com.adarshr.test-logger") version "4.0.0"
}

group = "com.benjamin"
version = "0.0.1-SNAPSHOT"


java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.postgresql:postgresql")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging.showStandardStreams = true

	testlogger {
		theme = ThemeType.STANDARD
		showExceptions = true
		showStackTraces = true
		showFullStackTraces = false
		showCauses = true
		slowThreshold = 2000
		showSummary = true
		showSimpleNames = false
		showPassed = true
		showSkipped = true
		showFailed = true
		showOnlySlow = false
		showStandardStreams = false
		showPassedStandardStreams = true
		showSkippedStandardStreams = true
		showFailedStandardStreams = true
		logLevel = LogLevel.LIFECYCLE
	}
}

val databaseName by extra("java-challenge-database")


tasks.register("databaseStart", Exec::class) {
	group = "Infrastructure"
	description = "Start the application PostgreSQL database"
	commandLine("docker", "run", "-d", "--name", databaseName,
		"-e", "POSTGRES_USER=java-challenge",
		"-e", "POSTGRES_PASSWORD=java-challenge",
		"-e", "POSTGRES_DB=java-challenge",
		"-p", "5432:5432", "postgres:latest")

}

tasks.register("databaseStop", Exec::class) {
	group = "Infrastructure"
	description = "Stop and remove the application PostgreSQL database"
	commandLine("docker", "rm", "-f", databaseName)
}

tasks.register("infraStart", Exec::class) {
	group="Infrastructure"
	description = "Deploy local infrastructure with database and application running"
	commandLine("docker", "compose" ,"up", "-d")
}

tasks.register("infraStop", Exec::class) {
	group="Infrastructure"
	description = "Stop local infrastructure"
	commandLine("docker", "compose" ,"down")
}

