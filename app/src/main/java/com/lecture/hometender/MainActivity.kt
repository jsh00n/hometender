package com.lecture.hometender

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.FileProvider
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.lecture.hometender.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var imageFilePath: String? = null
    private var photoUri: Uri? = null

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageView: ImageView // ImageView 추가

    private val startActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Image capture successful, display the captured image
            imageView.setImageURI(photoUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageView = findViewById(R.id.iv) // ImageView 초기화

        binding.btnCapture.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    try {
                        val file: File = createImageFile(this@MainActivity)
                        photoUri = FileProvider.getUriForFile(applicationContext, packageName, file)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        startActivityResult.launch(intent)
                    } catch (ex: IOException) {
                        // Handle the error
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(activity: AppCompatActivity): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "TEST_$timeStamp"
        val storageDir: File? = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
    }
}