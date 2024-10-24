plugins {
    `java-library`
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"

    id("fabric-loom") version "1.8.+"
}

val mcVersion = property("mcVersion")!!.toString()
val mcSemverVersion = stonecutter.current.version
val mcDep = property("fmj.mcDep").toString()

group = "dev.isxander"
val versionWithoutMC = "0.0.1"
version = "$versionWithoutMC+${stonecutter.current.project}"

base {
    archivesName.set(property("modName").toString())
}

loom {
    if (stonecutter.current.isActive) {
        runConfigs.all {
            ideConfigGenerated(true)
            runDir("../../run")
        }
    }

    mixin {
        useLegacyMixinAp.set(false)
    }
}

stonecutter {
    swap(
        "fov-precision",
        if (stonecutter.eval(stonecutter.current.version, ">=1.21.2"))
            "float" else "double"
    )
}

repositories {
    maven("https://nexus.flawcra.cc/repository/maven-mirrors/")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.layered {
        officialMojangMappings()
    })
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabricLoader")}")

    val fapiVersion = property("deps.fabricApi").toString()
    listOf(
        "fabric-resource-loader-v0",
        "fabric-lifecycle-events-v1",
        "fabric-key-binding-api-v1",
        "fabric-command-api-v2",
    ).forEach {
        modImplementation(fabricApi.module(it, fapiVersion))
    }
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:$fapiVersion") // so you can do `depends: fabric-api` in FMJ
    modImplementation("net.fabricmc:fabric-language-kotlin:${property("deps.flk")}")

    modApi("dev.isxander:yet-another-config-lib:${property("deps.yacl")}") {
        // was including old fapi version that broke things at runtime
        exclude(group = "net.fabricmc.fabric-api", module = "fabric-api")
    }

    // mod menu compat
    optionalProp("deps.modMenu") {
        modImplementation("com.terraformersmc:modmenu:$it")
    }
}

tasks {
    processResources {
        val modId: String by project
        val modName: String by project
        val modDescription: String by project

        val props = mapOf(
            "id" to modId,
            "group" to project.group,
            "name" to modName,
            "description" to modDescription,
            "version" to project.version,
            "mc" to mcDep
        )

        props.forEach(inputs::property)

        filesMatching("fabric.mod.json") {
            expand(props)
        }

        eachFile {
            // don't include photoshop files for the textures for development
            if (name.endsWith(".psd")) {
                exclude()
            }
        }
    }
}

val javaMajorVersion = property("java.version").toString().toInt()
java {
    withSourcesJar()

    javaMajorVersion
        .let { JavaVersion.values()[it - 1] }
        .let {
            sourceCompatibility = it
            targetCompatibility = it
        }
}

tasks.withType<JavaCompile> {
    options.release = javaMajorVersion
}
kotlin {
    jvmToolchain(javaMajorVersion)
}

fun <T> optionalProp(property: String, block: (String) -> T?) {
    findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)
}
