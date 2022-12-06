package com.example.nojoto

data class Result(
    val imageLqThumbName: String,
    val imageLqThumbUrl: String,
    val imageName: String,
    val imageSizes: List<ImageSize>,
    val imageUrl: String,
    val isMultisizeImg: Int,
    val isWebP: Int
)