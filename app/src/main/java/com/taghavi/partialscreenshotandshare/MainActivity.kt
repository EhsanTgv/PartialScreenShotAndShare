package com.taghavi.partialscreenshotandshare

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.provider.MediaStore.Images
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.taghavi.partialscreenshotandshare.databinding.ActivityMainBinding
import java.io.OutputStream


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            sampleConstraintLayout.setOnClickListener {
                val bitmap = getBitmapFromView(sampleConstraintLayout)
                showingResult.setImageBitmap(bitmap)

                val share = Intent(Intent.ACTION_SEND)
                share.type = "image/jpeg"

                val values = ContentValues()
                values.put(Images.Media.TITLE, "title")
                values.put(Images.Media.MIME_TYPE, "image/jpeg")
                val uri = contentResolver.insert(
                    Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )


                try {
                    val outStream = contentResolver.openOutputStream(uri!!)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                    outStream!!.close()
                } catch (e: Exception) {
                    System.err.println(e.toString())
                }

                share.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(share, "Share Image"))
            }
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}