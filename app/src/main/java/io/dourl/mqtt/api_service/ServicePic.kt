package io.dourl.mqtt.api_service
import com.example.videoapp.model.VideoModel
import io.dourl.http.coroutine.ApiResponse
import io.dourl.mqtt.BuildConfig
import io.dourl.mqtt.PicsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ServicePic {

    @GET("/api/")
    suspend fun getImage(
        @Query("key")key: String = BuildConfig.API_KEY,
        @Query("q") q: String,
        @Query("lang") lang: String = "pt"
    ): ApiResponse<PicsModel>

    @GET("/api/videos")
    suspend fun getVideo(
        @Query("key") key: String = BuildConfig.API_KEY,
        @Query("q") q: String,
        @Query("lang") lang: String = "pt"
    ): ApiResponse<VideoModel>
}