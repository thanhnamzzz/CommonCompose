package common.commons_compose.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel(){
	init {
		Log.i("Namzzz", "HomeViewModel: init")
	}
	fun testFunction() {
		Log.d("Namzzz", "HomeViewModel: testFunction")
	}
}