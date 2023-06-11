package com.capstonebangkit.siboeah

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.IOException

class PemindaiBuahActivity : AppCompatActivity() {


    private lateinit var camera: Button
    private lateinit var gallery: Button
    private lateinit var imageView: ImageView
    private lateinit var result: TextView
    private val imageSize = 32

    private val BASE_URL = "https://capstone-389205-default-rtdb.asia-southeast1.firebasedatabase.app/"

    private lateinit var retrofit: Retrofit
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemindai_buah)

        camera = findViewById(R.id.button)
        gallery = findViewById(R.id.button2)

        result = findViewById(R.id.result)
        imageView = findViewById(R.id.imageView)

        camera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 3)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
        gallery.setOnClickListener {
            val cameraIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(cameraIntent, 1)
        }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        FirebaseApp.initializeApp(this) // Initialize Firebase
    }




    private fun kirimImageToAPI(image: Bitmap) {
        val base64Image = bitmapToBase64(image)
        val chat = UploadResponse(base64Image)
        val call: Call<Void> = apiService.postGambar(chat)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Handle successful response
                    Toast.makeText(applicationContext, "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                    uploadToFirebaseStorage(image)


                } else {
                    // Handle error or unsuccessful response
                    Toast.makeText(applicationContext, "Failed to response", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle failure
                Toast.makeText(applicationContext, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun processImage(image: Bitmap) {
        val dimension = Math.min(image.width, image.height)
        val thumbnail = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
        imageView.setImageBitmap(thumbnail)

        val scaledImage = Bitmap.createScaledBitmap(thumbnail, imageSize, imageSize, false)

        kirimImageToAPI(scaledImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                val image = data?.extras?.get("data") as Bitmap
                processImage(image)
            } else if (requestCode == 1) {
                val dat: Uri? = data?.data
                var image: Bitmap? = null
                try {
                    image = MediaStore.Images.Media.getBitmap(this.contentResolver, dat)
                    if (image != null) {
                        processImage(image)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    private fun uploadToFirebaseStorage(selectedImage: Bitmap) {
        val storage = Firebase.storage("gs://capstone-389205.appspot.com")
        val storageRef = storage.reference

        // Create a unique filename for the image
        val filename = "image_${System.currentTimeMillis()}.jpg"

        // Create a reference to the image file
        val imageRef = storageRef.child(filename)

        // Convert the Bitmap to a byte array
        val baos = ByteArrayOutputStream()
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        // Upload the image file to Firebase Storage
        val uploadTask = imageRef.putBytes(imageData)
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Image uploaded successfully
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                // Get the download URL of the uploaded image
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    // Use the URL of the image (e.g., display it in an ImageView)
                    imageView.setImageURI(uri)

                    // Save the image URL to Realtime Database
                    val database = FirebaseDatabase.getInstance()
                    val databaseRef = database.reference
                    val imageRef = databaseRef.child("images").push() // Generate a unique key
                    imageRef.setValue(imageUrl)
                }.addOnFailureListener { exception ->
                    // Handle any errors that occur while getting the download URL
                    Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle failures
                Toast.makeText(this, "Failed upload to storage", Toast.LENGTH_SHORT).show()
            }
        }
    }




}