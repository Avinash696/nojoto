package com.example.nojoto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.nojoto.databinding.ActivityUploadBinding

class UploadActivity : AppCompatActivity() {
    private val TAG = "uploadTag"
    private lateinit var binding: ActivityUploadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Demo Module"

        binding.bottomNavUpload.setOnItemSelectedListener {
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
}