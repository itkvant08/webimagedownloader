package com.example.webimagedownloader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.net.URL

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageView: ImageView = findViewById(R.id.image_frame)
        val urlField: EditText = findViewById(R.id.image_link)
        val downloadButton: Button = findViewById(R.id.download_button)

        downloadButton.setOnClickListener {
            val url = urlField.text.toString()
            urlField.text.clear()

            if (URLUtil.isValidUrl(url)) {
                val job: Job = GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val bitmap: Bitmap = loadImageFromNetwork(url)
                        imageView.post { imageView.setImageBitmap(bitmap) }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Произошла непредвиденная ошибка при загрузке изображения", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                job.start()
            } else {
                //вернул false что говорит о том, что URL некорректный
                Toast.makeText(this, "Введен некорректный URL", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loadImageFromNetwork(link: String): Bitmap {
        val connection = URL(link).openConnection()
        val inputStream = connection.getInputStream()
        val image = BitmapFactory.decodeStream(inputStream)
        return image
    }
}