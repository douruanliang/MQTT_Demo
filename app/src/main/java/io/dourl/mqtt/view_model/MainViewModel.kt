package io.dourl.mqtt.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videoapp.model.VideoConfig
import io.dourl.http.coroutine.*
import io.dourl.mqtt.Pics
import io.dourl.mqtt.PicsModel
import io.dourl.mqtt.PicsRepository
import io.dourl.mqtt.utils.log.LoggerUtil
import kotlinx.coroutines.launch


class MainViewModel constructor(private val repository: PicsRepository) : ViewModel() {

    private val _IMG = MutableLiveData<List<Pics>>()

    val pics: LiveData<List<Pics>> = _IMG

    private val _VIDEOS = MutableLiveData<List<VideoConfig>>()

    val videos: LiveData<List<VideoConfig>> = _VIDEOS
    val toastLiveData = MutableLiveData<String>()

    private val home = listOf("Vida", "Natureza", "Praia", "Lua")



    fun getAllPics(query: String = home.random()) {
        viewModelScope.launch {
            repository.getAllPicsService(query = query)
                .suspendOnSuccess {
                    _IMG.value = data.hits
                    LoggerUtil.d("home", "_IMG.value"+_IMG.value)
                }
                .onError {
                    val message = when (statusCode) {
                        ResponseStatusCode.InternalServerError -> "InternalServerError"
                        ResponseStatusCode.BadGateway -> "BadGateway"
                        else -> "$statusCode(${statusCode.code}): ${message()}"
                    }
                    toastLiveData.postValue(message)
                }
                .onException {
                    toastLiveData.postValue(message)
                }

        }
    }

    fun getVideo(query: String = home.random()) {
        /*viewModelScope.launch {
            repository.getALlVideoService(query = query).let {
                _VIDEOS.value = it
            }
        }*/
    }

}
