package kids.baba.mobile.presentation.viewmodel

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canhub.cropper.CropImageView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kids.baba.mobile.domain.model.MediaData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CropViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val TAG = "CropViewModel"

    val currentTakenMediaInCrop = savedStateHandle.get<MediaData>(MEDIA_DATA)
    val currentTakenMedia = MutableStateFlow<MediaData?>(savedStateHandle[MEDIA_DATA])

    fun cropImage(cropImageView: CropImageView) = callbackFlow {
        viewModelScope.launch {
            cropImageView.setOnCropImageCompleteListener { _, result ->
                Log.d(
                    TAG, "CropResult - original uri : ${result.originalUri}" +
                            "  cropped content: ${result.uriContent}"
                )

                val fileName = result.uriContent.toString().split("/").last()
                val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val file = File(storageDir, fileName)

                currentTakenMedia.value =
                    MediaData(
                        mediaName = "Cropped",
                        mediaUri = file.absolutePath
                    )

                trySendBlocking(currentTakenMedia.value!!)
            }
            cropImageView.croppedImageAsync()

        }
        awaitClose()
    }

    companion object {
        const val MEDIA_DATA = "mediaData"
    }

}