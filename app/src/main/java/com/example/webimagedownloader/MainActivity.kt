package com.example.webimagedownloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.URL

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView: ImageView = findViewById(R.id.image_frame)
        val urlField: EditText = findViewById(R.id.image_link)
        val downloadButton: Button = findViewById(R.id.download_button)

        downloadButton.setOnClickListener {
            val job: Job = GlobalScope.launch(Dispatchers.IO) {
                val bitmap: Bitmap = loadImageFromNetwork(urlField.text.toString())
                imageView.post { imageView.setImageBitmap(bitmap) }
            }
            job.start()
        }
    }

    private fun loadImageFromNetwork(link: String): Bitmap {
        val connection = URL(link).openConnection()
        val inputStream = connection.getInputStream()
        return try {
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            BitmapFactory.decodeResource(getResources(), R.drawable.notanimage)
        }
    }
}