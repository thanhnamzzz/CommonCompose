package common.commons_compose.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LiquidViewModel @Inject constructor() : ViewModel(){
	init {
		Log.i("Namzzz", "LiquidViewModel: init")
	}
	fun testFunction() {
		Log.d("Namzzz", "LiquidViewModel: testFunction")
	}
}