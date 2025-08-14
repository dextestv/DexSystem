# Использование Dex Loader для создания плагинов

Скачайте и установите [Android Studio](https://developer.android.com/studio)

Скачайте и распакуйте исходники [Telegram'a](https://github.com/DrKLO/Telegram)

Откройте исходники как проект в Android Studio

Создайте новый Android Library(File -> New -> New module -> Android Library)

## Инструкции ниже только для Groovy DSL(не Kotlin)
### build.gradle
Дальше необходимо указать зависимости:

    compileOnly project(':TMessagesProj')
    compileOnly 'com.aliucord:Aliuhook:1.1.3'

    Это основные зависимости, остальные можно указывать по желанию

    compileOnly - зависимость не собирается в конечный файл, использовать только для того, что уже есть в Telegram/Exteragram, к примеру: Aliuhook уже есть, и нам не надо собирать его вместе с файлом
    implementation - зависимость включается в конечный файл

В репозиториях необходимо указать aliuhook

    maven { url 'https://maven.aliucord.com/snapshots' }






## Настройка Gradle для быстрого получения .dex файла(by ChatGPT)
    tasks.register("aarToDex", Exec) {
    dependsOn "assembleRelease"
    group = "build"
    description = "Собирает DEX из AAR модуля"

    doFirst {
        def aarPath = "$buildDir/outputs/aar/${project.name}-release.aar"
        def dexOut = "$buildDir/dex/"
        mkdir dexOut

        commandLine "${android.sdkDirectory}/build-tools/35.0.1/d8.bat", // Поменяйте на свой путь к d8
                aarPath,
                "--output", dexOut
    }
    }


    tasks.register("buildDex") {
        group = "build"
        description = "Собирает модуль и конвертирует его в DEX"
        dependsOn "assembleRelease", "aarToDex"
    }
После запускаем buildDex либо через Android Studio, ибо через gradlew.

    .\gradlew buildDex

Готовый файл будет лежать в $modulename/build/dex

Готовый файл:

    Для файла с package: b.testFragment и основным классом(который вы хотите загружать) testFragment, загрузка будет выглядеть как: .dexload b.testFragment.testFragment $url
    Запуск класса всегда начинается с метода start(), который не должен принимать аргументы

