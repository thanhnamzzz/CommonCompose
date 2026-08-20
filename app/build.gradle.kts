plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.dagger.hilt)
	id("com.google.devtools.ksp")
}

android {
	namespace = "common.commons_compose"
	compileSdk {
		version = release(37) {
			minorApiLevel = 1
		}
	}

	defaultConfig {
		applicationId = "common.commons_compose"
		minSdk = 23
		targetSdk = 37
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	buildFeatures {
		compose = true
	}
}

dependencies {

	implementation(project(":libs_compose"))

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.compose.ui.graphics)
	implementation(libs.androidx.compose.ui.tooling.preview)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.material.icons.extended)
	implementation(libs.androidx.navigation3.ui.android)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.androidx.lifecycle.viewmodel.navigation3)

	implementation("io.coil-kt.coil3:coil-compose:3.5.0")
	implementation(libs.lottie.compose)
	//Crop Image
	implementation("io.github.mr0xf00:easycrop:0.1.1")

	//Color picker
	implementation("com.github.skydoves:colorpicker-compose:1.2.0")
	implementation(libs.androidx.compose.runtime)
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	debugImplementation(libs.androidx.compose.ui.tooling)
	debugImplementation(libs.androidx.compose.ui.test.manifest)

	//Haze blur background
	implementation("dev.chrisbanes.haze:haze:1.7.2")
	implementation("dev.chrisbanes.haze:haze-materials:1.7.2")

	//Hilt
	implementation(libs.hilt.android)
	ksp(libs.hilt.android.compiler)
	ksp("org.jetbrains.kotlin:kotlin-metadata-jvm:2.4.0")
	implementation(libs.androidx.hilt.navigation.compose)
}