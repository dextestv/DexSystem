# Using Dex Loader to Create Plugins

Download and install Android Studio.

Download and unpack the Telegram source code.

Open the source code as a project in Android Studio.

Create a new Android Library (File -> New -> New module -> Android Library).

## The instructions below are for Groovy DSL only (not Kotlin)
### build.gradle

Next, you need to specify the dependencies:
groovy

    compileOnly project(':TMessagesProj')
    compileOnly 'com.aliucord:Aliuhook:1.1.3'


    These are the core dependencies; you can add others as needed.

    compileOnly: The dependency is not compiled into the final file. Use this only for libraries that already exist in Telegram/Exteragram. For example, Aliuhook is already included, so we don't need to bundle it with our file.
    implementation: The dependency is included in the final file.

You must add the aliuhook repository:

    maven { url 'https://maven.aliucord.com/snapshots' }

Configuring Gradle for Quick .dex File Generation (by ChatGPT)
groovy

tasks.register("aarToDex", Exec) {
    dependsOn "assembleRelease"
    group = "build"
    description = "Builds a DEX file from the module's AAR."

    doFirst {
        def aarPath = "$buildDir/outputs/aar/${project.name}-release.aar"
        def dexOut = "$buildDir/dex/"
        mkdir dexOut

        commandLine "${android.sdkDirectory}/build-tools/35.0.1/d8.bat", // Change this to your d8 path
                aarPath,
                "--output", dexOut
    }
    }

    tasks.register("buildDex") {
        group = "build"
        description = "Builds the module and converts it to a DEX file."
        dependsOn "assembleRelease", "aarToDex"
    }

After adding this, run buildDex either through the Android Studio Gradle panel or via the command line.
shell

    .\gradlew buildDex

The finished file will be located in $modulename/build/dex.

Using the Final File

    For a file with the package b.testFragment and a main class (the one you want to load) named testFragment, the loading command would look like this:
    plaintext

    .dexload b.testFragment.testFragment $url

    The execution of the class always begins with the start() method, which must not accept any arguments.
