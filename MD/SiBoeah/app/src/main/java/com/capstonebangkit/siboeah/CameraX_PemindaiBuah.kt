package com.capstonebangkit.siboeah

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.capstonebangkit.siboeah.ui.theme.SiboeahTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor


@OptIn(ExperimentalAnimationApi::class)
class CameraX_PemindaiBuahActivity : ComponentActivity() {
    private val buahTelahDipindaiViewModel: BuahTelahDipindaiViewModel by viewModels()
    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, initialize the camera preview
            initializeCameraPreview()
        } else {
            // Permission denied, handle accordingly (e.g., show error message)
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private var imageCapture: ImageCapture? = null
    private var imageCaptureOutputOptions: ImageCapture.OutputFileOptions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SiboeahTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraXPreviewAndCapture()
                }
            }
        }

        if (isCameraPermissionGranted()) {
            initializeCameraPreview()
        } else {
            requestCameraPermission()
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        permissionRequest.launch(Manifest.permission.CAMERA)
    }

    private fun initializeCameraPreview() {
        setContent {
            SiboeahTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraXPreviewAndCapture()
                }
            }
        }
    }

    data class BuahTelahDipindai(
        val namaBuah: String,
        val gambarBuah: String,
        val tingkatKesegaran: String,
        val informasiBuah: String,
        val kaloriEnergi: String,
        val kandungan: String,
        val penyimpanan: String
    )

    private fun captureImage() {
        Toast.makeText(this@CameraX_PemindaiBuahActivity, "Jepret Berhasil", Toast.LENGTH_SHORT).show()
        val outputDirectory = getOutputDirectory()
        val imageCapture = imageCapture ?: return

        val imageFileName =
            "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}"
        val photoFile = File(outputDirectory, "$imageFileName.jpg")

        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        this.imageCaptureOutputOptions = outputFileOptions

        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val imageFiles = outputFileResults.savedUri?.toFile()

                    // Compress the image file
//                    val compressedImageFile = withContext(Dispatchers.Default) {
//                        Compressor.compress(context, imageFiles) {
//                            quality(80) // Adjust the quality as per your requirement
//                            maxSize(1_000_000) // Set the maximum file size (in bytes)
//                        }
//                    }

                    // Use the compressed image file for further processing
                    val imageFile = imageFiles


                    // Obtain other required information for BuahTelahDipindai
                    val namaBuah = "Nama Buah"
                    val tingkatKesegaran = "Tingkat Kesegaran"
                    val informasiBuah = "Informasi Buah"
                    val kaloriEnergi = "Kalori Energi"
                    val kandungan = "Kandungan"
                    val penyimpanan = "Penyimpanan"

                    // Create an instance of BuahTelahDipindai
                    val buahTelahDipindai = BuahTelahDipindai(
                        namaBuah = namaBuah,
                        gambarBuah = imageFile?.path ?: "",
                        tingkatKesegaran = tingkatKesegaran,
                        informasiBuah = informasiBuah,
                        kaloriEnergi = kaloriEnergi,
                        kandungan = kandungan,
                        penyimpanan = penyimpanan
                    )


                    // Assign the created instance to the mutable state variable
                    buahTelahDipindaiViewModel.setBuahTelahDipindai(buahTelahDipindai, photoFile)
                    buahTelahDipindaiViewModel.setCardVisibleState(true)
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraX_PemindaiBuahActivity,
                        "Failed to capture image",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    class BuahTelahDipindaiViewModel : ViewModel() {
        private var _cardVisibleState by mutableStateOf(false)
        val cardVisibleState: Boolean
            get() = _cardVisibleState

        private var _photoFile by mutableStateOf<File?>(null)
        val photoFile: File?
            get() = _photoFile

        fun setBuahTelahDipindai(buahTelahDipindai: BuahTelahDipindai, photoFile: File) {
            _cardVisibleState = true
            _photoFile = photoFile

            // Store the captured image in the BuahTelahDipindai instance
            // buahTelahDipindaiViewModel.setBuahTelahDipindai(buahTelahDipindai)
        }

        fun setCardVisibleState(visible: Boolean) {
            _cardVisibleState = visible
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return mediaDir ?: filesDir
    }

    @Composable
    fun CameraXPreviewAndCapture() {
        val buahTelahDipindaiViewModel: BuahTelahDipindaiViewModel = viewModel()
        val cardVisibleState = buahTelahDipindaiViewModel.cardVisibleState
        val photoFile = buahTelahDipindaiViewModel.photoFile
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current

        val cameraExecutor = remember { ContextCompat.getMainExecutor(context) }
        val cameraSelector = remember { CameraSelector.DEFAULT_BACK_CAMERA }

        val previewView = remember { PreviewView(context) }

        AndroidView(
            factory = { context ->
                previewView.apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    bindCameraPreview(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        cameraProvider = cameraProvider,
                        cameraSelector = cameraSelector,
                        previewView = previewView,
                        cameraExecutor = cameraExecutor
                    )
                }, cameraExecutor)
            }
        )

        Column(modifier = Modifier) {
            Button(
                onClick = { captureImage() },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Jepret")
            }

            AnimatedVisibility(
                visible = cardVisibleState,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    val imageCaptureOutputLocation = photoFile?.path
                    if (imageCaptureOutputLocation != null) {
                        Image(
                            painter = rememberImagePainter(imageCaptureOutputLocation),
                            contentDescription = "Captured Image",
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(text = "No image captured")
                    }
                }
            }
        }
    }

    private fun bindCameraPreview(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        cameraProvider: ProcessCameraProvider,
        cameraSelector: CameraSelector,
        previewView: PreviewView,
        cameraExecutor: Executor
    ) {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        val camera = cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        this.imageCapture = imageCapture

        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                cameraProvider.unbindAll()
            }
        })
    }
}

