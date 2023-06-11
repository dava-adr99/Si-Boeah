package com.capstonebangkit.siboeah

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("gambar.json")
    fun postGambar(@Body uploadResponse: UploadResponse): Call<Void>
}


