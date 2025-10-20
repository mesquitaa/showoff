configurations {
  create("ktlint")
}

dependencies {
  "ktlint"("com.pinterest.ktlint:ktlint-cli:1.5.0")
}

tasks.register<JavaExec>("ktlint") {
    group = "verification"
    description = "Check Kotlin code style."
    classpath = configurations.getByName("ktlint")
    mainClass.set("com.pinterest.ktlint.Main")
    args(
        "src/**/*.kt",
        "--reporter=plain",
        "--reporter=checkstyle,output=${layout.buildDirectory.get().asFile}/reports/ktlint/ktlint-checkstyle.xml"
    )
}

tasks.named("check") {
  dependsOn("ktlint")
}

tasks.register<JavaExec>("ktlintFormat") {
  group = "formatting"
  description = "Fix Kotlin code style deviations."
  classpath = configurations.getByName("ktlint")
  mainClass.set("com.pinterest.ktlint.Main")
  args("-F", "src/**/*.kt")
}
