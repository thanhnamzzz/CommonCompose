package common.commons_compose

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApp : Application(){
	override fun onCreate() {
		super.onCreate()
		Log.i("Namzzz", "MainApp: onCreate")
	}
}