plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.compose)
	`maven-publish`
}

android {
	namespace = "common.libs.compose"
	compileSdk {
		version = release(36) {
			minorApiLevel = 1
		}
	}

	defaultConfig {
		minSdk = 23

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
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

	buildFeatures { compose = true }

	publishing {
		singleVariant("release")
	}
}

dependencies {
	implementation(libs.androidx.compose.material3)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.navigation3.ui.android)

	//LiquidGlass
	api(libs.backdrop)
//	implementation(libs.capsule)
}

afterEvaluate {
	publishing {
		publications {
			create<MavenPublication>("release") {
				from(components["release"])
				groupId = "common-libs"
				artifactId = "compose"
				version = "1.0"
			}
		}
	}
}