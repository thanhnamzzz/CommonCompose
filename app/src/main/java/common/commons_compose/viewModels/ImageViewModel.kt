package common.commons_compose.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor() : ViewModel(){
	init {
		Log.i("Namzzz", "ImageViewModel: init")
	}
	fun testFunction() {
		Log.d("Namzzz", "ImageViewModel: testFunction")
	}
}