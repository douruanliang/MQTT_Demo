package io.dourl.mqtt

import io.dourl.http.coroutine.ApiResponse
import io.dourl.mqtt.api_service.ServicePic
import retrofit2.Response


class PicsRepository constructor(private val servicePic: ServicePic) {


    suspend fun getAllPicsService(query: String): ApiResponse<PicsModel> =
        servicePic.getImage(q = query)

    /* suspend fun getAllPicsServiceOld(query: String): ApiResponse<List<Pics>>? {
         return withContext(Dispatchers.Default) {
             val response = servicePic.getImage(q = query)
             val processesResponse = processData(response)
             processesResponse?.hits
         }
     }*/

    /* suspend fun getALlVideoService(query: String): List<VideoConfig>?{
         return withContext(Dispatchers.Default){
             val response = servicePic.getVideo(q = query)
             val processResponse = processData(response)
             processResponse?.hits
         }
     }*/


    private fun <T> processData(response: Response<T>): T? {
        return if (response.isSuccessful) response.body() else null
    }
}