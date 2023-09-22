package com.example.fishknowconnect.network

import com.example.fishknowconnect.ui.login.LoginResponse
import com.example.fishknowconnect.ui.newPost.NewPostResponse
import com.example.fishknowconnect.ui.register.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

//private const val BASE_URL = "http://13.236.94.194:3000/"
private const val BASE_URL = "http://10.0.2.2:3000/"
private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
private val client = OkHttpClient.Builder().addInterceptor(logging).build()
private val retrofit =
    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
        .client(client).build()

interface FishKnowConnectApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String, @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("phone") phone: String,
        @Field("ID") id: String
    ): Response<RegisterResponse>


    @Multipart
    @POST("create_post")
    suspend fun createPost(
        @Part("title") title: String,
        @Part("type") type: String,
        @Part("content") content: String,
        @Part file: MultipartBody.Part?
    ): Response<NewPostResponse>
}

object FishKnowConnectApi {
    val retrofitService: FishKnowConnectApiService by lazy {
        retrofit.create(FishKnowConnectApiService::class.java)
    }
}
