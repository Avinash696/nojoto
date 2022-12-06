package com.example.nojoto

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nojoto.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity() {

    private val TAG = "mainTag"
    lateinit var picLoc: Uri


    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Upload Photo Screen"
        cameraOpen()
//        binding.btnCamera.setOnClickListener {
//
//        }
        binding.btnUpload.setOnClickListener {
            progressBar()
            uploadImg()
        }
        binding.bottomNavMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_bottom_camera -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun cameraOpen() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 222)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === 222) {
            val pic = data!!.extras!!.get("data")
            binding.ivIMageTest.setImageBitmap(pic as Bitmap?)

            picLoc = getImageUri(this, pic!!)!!
            binding.btnCamera.visibility = View.INVISIBLE

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(uri: Uri?): String? {
        var path = ""
        if (contentResolver != null) {
            val cursor: Cursor? = contentResolver.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    private fun uploadImg() {
        val type = "media"
        val finalFile = File(getRealPathFromURI(picLoc))
        val call = RetrofitHelper.getClient().create(apiService::class.java)
        //convert
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), finalFile)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", finalFile.name, requestFile)

        val tag: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), type)
        Log.d(TAG, "call: ${call}")
        GlobalScope.launch(Dispatchers.Main) {
            val result = call.uploadUrImg(tag, body)
            Log.d(TAG, "result: $result")
            if (result.isSuccessful) {

                Toast.makeText(this@MainActivity, result.body()!!.message, Toast.LENGTH_SHORT)
                    .show()
//                Log.d(TAG, "success: ${result.body()}")
                binding.progressBar.visibility = View.INVISIBLE
            } else
                Toast.makeText(this@MainActivity, "${result.errorBody()}", Toast.LENGTH_SHORT)
                    .show()
//                Log.d(TAG, "error: ${result.errorBody()}")
        }
    }

    private fun progressBar() {

        for (i in 0..100) {
            binding.progressBar.progress = i
        }
    }
}