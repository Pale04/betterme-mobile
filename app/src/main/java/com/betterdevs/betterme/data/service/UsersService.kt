package com.betterdevs.betterme.data.service

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import com.betterdevs.betterme.BuildConfig
import com.betterdevs.betterme.data.dto.CreateAccountBodyDTO
import com.betterdevs.betterme.data.dto.CreateAccountResponseDTO
import com.betterdevs.betterme.data.dto.UpdateUserBodyDTO
import com.betterdevs.betterme.data.dto.UpdateUserEmailDTO
import com.betterdevs.betterme.data.dto.UserDTO
import com.google.gson.GsonBuilder
import retrofit2.http.Path
import java.time.Instant
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import okhttp3.HttpUrl
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UsersService {
    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: String): Response<UserDTO>

    @POST("api/users")
    suspend fun addUser(@Body user: CreateAccountBodyDTO): Response<CreateAccountResponseDTO>

    @PATCH("api/users/edit/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: UpdateUserBodyDTO): Response<CreateAccountResponseDTO>

    @PATCH("api/users/{id}/email")
    suspend fun updateUserEmail(@Path("id") id: String, @Body user: UpdateUserEmailDTO): Response<UpdateUserEmailDTO>


    companion object {
        fun create(context: Context): UsersService {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor(AuthInterceptor(context))
                .build()

            val baseUrl: HttpUrl = HttpUrl.Builder()
                .scheme("http")
                .host(BuildConfig.SERVER_BASE_IP)
                .port(BuildConfig.USERS_API_PORT)
                .build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(
                    GsonBuilder()
                        .registerTypeAdapter(Instant::class.java, JsonDeserializer { json, _, _ ->
                            Instant.parse(json.asString)
                        })
                        .registerTypeAdapter(Instant::class.java, JsonSerializer<Instant> { src, _, _ ->
                            JsonPrimitive(src.toString())
                        })
                        .create()
                ))
                .build()
                .create(UsersService::class.java)
        }
    }
}