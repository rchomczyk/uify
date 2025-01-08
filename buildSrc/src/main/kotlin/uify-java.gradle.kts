plugins {
    `java-library`
}

java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src"))
        resources.setSrcDirs(emptyList<String>())
    }
    test {
        java.setSrcDirs(emptyList<String>())
        resources.setSrcDirs(emptyList<String>())
    }
}