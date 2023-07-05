
package io.dourl.mqtt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.dourl.mqtt.network.NetworkModule
import io.dourl.mqtt.view_model.MainViewModel

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
      val mainRepository = PicsRepository(NetworkModule.picService)
      return MainViewModel(mainRepository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class.")
  }
}
