package common.commons_compose

import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.CropState
import com.mr0xf00.easycrop.CropperLoading
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.rememberImagePicker
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import common.libs.compose.toast.CToastConfiguration
import common.libs.compose.toast.CToastHost
import common.libs.compose.toast.CToastState
import common.libs.compose.toast.CToastType
import common.libs.compose.toast.LocalCToastConfig
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ContentCropImage(modifier: Modifier = Modifier) {
	var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }
	var error by remember { mutableStateOf<CropError?>(null) }
	val context = LocalContext.current
	val scope = rememberCoroutineScope()

	val imageCropper = rememberImageCropper()
	val imagePicker = rememberImagePicker(onImage = { uri ->
		scope.launch {
			when (val result = imageCropper.crop(uri, context)) {
				CropResult.Cancelled -> {}
				is CropError -> error = result
				is CropResult.Success -> {
					selectedImage = result.bitmap
				}
			}
		}
	})

	var imageUri by remember { mutableStateOf<Uri?>(null) }
	val imageFile = File.createTempFile(
		"scan_image",
		".jpg",
		context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
	)
	val cameraLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.TakePicture()
	) {
		scope.launch {
			when (val result = imageCropper.crop(imageUri!!, context)) {
				CropResult.Cancelled -> {}
				is CropError -> error = result
				is CropResult.Success -> {
					selectedImage = result.bitmap
				}
			}
		}
	}

	val cToastState = remember { CToastState() }

	CompositionLocalProvider(LocalCToastConfig provides CToastConfiguration()) {
		ContentCropImage(
			cropState = imageCropper.cropState,
			loadingStatus = imageCropper.loadingStatus,
			selectedImage = selectedImage,
			onPick = { imagePicker.pick() },
			onTakePicture = {
				imageUri = FileProvider.getUriForFile(
					context,
					"${context.packageName}.provider",
					imageFile
				)
				cameraLauncher.launch(imageUri!!)
			},
			modifier = modifier
		)

		error?.let {
			scope.launch {
				cToastState.setAndShow(
					title = "Error",
					message = when (it) {
						CropError.LoadingError -> "Error while opening the image !"
						CropError.SavingError -> "Error while saving the image !"
					},
					type = CToastType.ERROR
				)
			}
		}
		CToastHost(cToastState, Modifier.systemBarsPadding())
	}
}

@Composable
fun ContentCropImage(
	cropState: CropState?,
	loadingStatus: CropperLoading?,
	selectedImage: ImageBitmap?,
	onPick: () -> Unit,
	onTakePicture: () -> Unit,
	modifier: Modifier = Modifier,
) {
	if (cropState != null) {
		ImageCropperDialog(state = cropState)
	}
	if (cropState == null && loadingStatus != null) {
		LoadingDialog(status = loadingStatus)
	}
	Column(
		modifier = modifier.padding(16.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		if (selectedImage != null) Image(
			bitmap = selectedImage, contentDescription = null,
			modifier = Modifier.weight(1f)
		) else Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)) {
			Text("No image selected !")
		}
		Button(onClick = onPick) { Text("Choose Image") }
		Button(onClick = onTakePicture) { Text("Take Image") }
	}
}

@Composable
fun LoadingDialog(status: CropperLoading) {
	var dismissed by remember(status) { mutableStateOf(false) }
	if (!dismissed) Dialog(onDismissRequest = { dismissed = true }) {
		Surface(shape = MaterialTheme.shapes.small, shadowElevation = 6.dp) {
			Column(
				verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier.padding(16.dp)
			) {
				CircularProgressIndicator()
				Text(
					text = when (status) {
						CropperLoading.PreparingImage -> "Preparing Image"
						CropperLoading.SavingResult -> "Saving Result"
					}
				)
			}
		}
	}
}