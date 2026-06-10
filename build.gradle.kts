plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
	alias(libs.plugins.kotlin.serialization) apply false
	alias(libs.plugins.dagger.hilt) apply false
	id("com.google.devtools.ksp") version "2.3.9" apply false
}