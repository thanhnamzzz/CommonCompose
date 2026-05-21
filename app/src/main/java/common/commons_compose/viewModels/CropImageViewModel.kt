package common.commons_compose.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CropImageViewModel @Inject constructor() : ViewModel(){
	init {
		Log.i("Namzzz", "CropImageViewModel: init")
	}
	fun testFunction() {
		Log.d("Namzzz", "CropImageViewModel: testFunction")
	}
}