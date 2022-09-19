import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
}

android {
    namespace = "app.grapheneos.gmscompat.config"

    compileSdk = 33

    defaultConfig {
        minSdk = 33
        targetSdk = 33
        versionCode = 1
    }

    sourceSets.getByName("main") {
        manifest.srcFile("AndroidManifest.xml")
        resources.srcDir("../../gmscompat_config")
    }

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    val useKeystoreProperties = keystorePropertiesFile.canRead()

    if (useKeystoreProperties) {
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        signingConfigs {
            create("release") {
                storeFile = rootProject.file(keystoreProperties["storeFile"]!!)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            manifestPlaceholders["app_name"] = "GmsCompat config"
            if (useKeystoreProperties) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        getByName("debug") {
            isMinifyEnabled = true
            applicationIdSuffix = ".dev"
            manifestPlaceholders["app_name"] = "GmsCompat config dev"
        }
    }
}