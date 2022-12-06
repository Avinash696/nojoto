package com.example.nojoto

data class ImgUploadResponceModel(
    val cid: String,
    val description: String,
    val error: Boolean,
    val message: String,
    val result: Result,
    val statusCode: Int,
    val success: Boolean
)