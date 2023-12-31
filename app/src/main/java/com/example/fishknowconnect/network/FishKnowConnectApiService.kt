package com.example.fishknowconnect.network

import com.example.fishknowconnect.ui.GetPostTypeResponse
import com.example.fishknowconnect.ui.approvePostRequest.GetApprovalResponse
import com.example.fishknowconnect.ui.home.HomeApprovalCountResponse
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
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * apis
 */
private const val BASE_URL = "http://13.236.94.194:3000/"
private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
private val client = OkHttpClient.Builder().addInterceptor(logging).build()
private val retrofit =
    Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
        .client(client).build()

interface FishKnowConnectApiService {
    //for login
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String, @Field("password") password: String
    ): Response<LoginResponse>

    //for register
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("age") age: String,
        @Field("location") location: String
    ): Response<RegisterResponse>

    //for creating post
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
    suspend fun getAllPost(): Response<List<GetPostTypeResponse>>

    @GET("get_public_posts")
    suspend fun getAllPublicPost(): Response<List<GetPostTypeResponse>>

    //private post
    @GET("get_not_granted_posts")
    suspend fun getAllPrivatePost(
        @Query("user") username: String,
        @Query("type") type: String
    ): Response<List<GetPrivatePostResponse>>

    //all post by type
    @GET("get_granted_posts")
    suspend fun getPostsByType(
        @Query("user") username: String,
        @Query("type") type: String
    ): Response<List<GetPostTypeResponse>>

    // all profile posts
    @GET("get_posts")
    suspend fun getProfilePosts(@Query("author") username: String): Response<List<GetPostTypeResponse>>

    //delete post
    @DELETE("delete_post/{id}")
    suspend fun deletePost(@Path("id") id: String): Response<LoginResponse>

    //for requesting private post access
    @Multipart
    @PUT("request_access/{id}")
    suspend fun sendPostAccessRequest(
        @Path("id") id: String,
        @Part("requested") requested: RequestBody
    ): Response<GetPrivatePostAccessResponse>

    //for rejecting private post access
    @Multipart
    @PUT("reject_access/{id}")
    suspend fun sendRejectApproval(
        @Path("id") id: String,
        @Part("rejected") requested: RequestBody
    ): Response<GetPrivatePostAccessResponse>

    //for granting private post access
    @Multipart
    @PUT("grant_access/{id}")
    suspend fun sendGrantAccess(
        @Path("id") id: String,
        @Part("granted") requested: RequestBody
    ): Response<GetPrivatePostAccessResponse>


    //for getting all approval list
    @GET("get_approvals")
    suspend fun getApprovals(
        @Query("user") users: String,
    ): Response<List<GetApprovalResponse>>

    //get profile list
    @GET("get_profile?")
    suspend fun getProfileInfo(
        @Query("username") username: String
    ): Response<ProfileResponse>

    //get approval request count
    @GET("get_approval_count")
    suspend fun getApprovalRequestsCount(
        @Query("user") username: String
    ): Response<HomeApprovalCountResponse>
}

object FishKnowConnectApi {
    val retrofitService: FishKnowConnectApiService by lazy {
        retrofit.create(FishKnowConnectApiService::class.java)
    }
}
