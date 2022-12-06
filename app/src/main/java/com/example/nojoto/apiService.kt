package com.example.nojoto

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface apiService {

    @Multipart
    @POST("content.php?cid=7ec99b415af3e88205250e3514ce0fa7")
    suspend fun uploadUrImg(
        @Part("type") type: RequestBody,
        @Part pic: MultipartBody.Part
    ): Response<ImgUploadResponceModel>
}