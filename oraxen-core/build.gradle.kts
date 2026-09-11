java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    // Adventure compileOnly - shared text/serializer, version provided by platforms
    compileOnly("net.kyori:adventure-api:4.18.0")
    compileOnly("net.kyori:adventure-text-minimessage:4.18.0")
    compileOnly("net.kyori:adventure-text-serializer-plain:4.18.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.18.0")
    compileOnly("net.kyori:adventure-text-serializer-ansi:4.18.0")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    compileTestJava {
        options.encoding = Charsets.UTF_8.name()
    }
}