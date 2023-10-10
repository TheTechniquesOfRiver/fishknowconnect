package com.example.fishknowconnect.network

import com.example.fishknowconnect.ui.fish.GetAllPostResponse
import com.example.fishknowconnect.ui.login.LoginResponse
import com.example.fishknowconnect.ui.newPost.NewPostResponse
import com.example.fishknowconnect.ui.privatePost.GetPrivatePostAccessResponse
import com.example.fishknowconnect.ui.privatePost.GetPrivatePostResponse
import com.example.fishknowconnect.ui.profile.ProfileResponse
import com.example.fishknowconnect.ui.register.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "http://13.236.94.194:3000/"

//private const val BASE_URL = "http://127.0.0.1:3000/"
//private const val BASE_URL = "http://10.0.2.2:3000/"
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
        @Field("age") age: String,
        @Field("location") location: String
    ): Response<RegisterResponse>

    @Multipart
    @POST("create_post")
    suspend fun createPost(
        @Part("title") title: RequestBody,
        @Part("type") type: RequestBody,
        @Part("content") content: RequestBody,
        @Part file: MultipartBody.Part?,
        @Part("fileType") fileType: RequestBody,
        @Part("access") access: RequestBody,
        @Part("author") author: RequestBody,
        ): Response<NewPostResponse>

    @GET("get_all_posts")
    suspend fun getAllPost(): Response<List<GetAllPostResponse>>

    @GET("get_public_posts")
    suspend fun getAllPublicPost(): Response<List<GetAllPostResponse>>

    @GET("get_private_posts")
    suspend fun getAllPrivatePost(): Response<List<GetPrivatePostResponse>>

    @GET("get_private_posts")
    suspend fun getAllProfilePostList(): Response<List<GetAllPostResponse>>

    @DELETE("delete_post/{id}")
    suspend fun deletePost(@Path("id") id:String): Response<LoginResponse>
    @POST("send_access_request")
    suspend fun sendPostAccessRequest(
        @Field("_id") _id: String
    ): Response<GetPrivatePostAccessResponse>

    @GET("get_profile?")
    suspend fun getProfileInfo(
        @Query("username") username: String
    ): Response<ProfileResponse>
}

object FishKnowConnectApi {
    val retrofitService: FishKnowConnectApiService by lazy {
        retrofit.create(FishKnowConnectApiService::class.java)
    }
}
