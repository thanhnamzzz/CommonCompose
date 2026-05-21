package common.commons_compose.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LiquidLazyViewModel @Inject constructor() : ViewModel(){
	init {
		Log.i("Namzzz", "LiquidLazyViewModel: init")
	}
	fun testFunction() {
		Log.d("Namzzz", "LiquidLazyViewModel: testFunction")
	}
}