package com.example.clonepexel.data.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clonepexel.data.response.Photo
import com.example.clonepexel.data.service.PexelsService.getPexelsApi
import com.example.clonepexel.data.util.Constants.NO_INTERNET
import com.example.clonepexel.util.StateApi
import kotlinx.coroutines.launch

class PexelsViewModel : ViewModel() {
    val pexelsPhotosLiveData: MutableLiveData<List<Photo>> = MutableLiveData()
    val stateApi: MutableLiveData<StateApi> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()
    private val perPage = 20
    private var currentPage = 1
    fun fetchData(isAvailableInternet : Boolean) {
        if (isAvailableInternet){
            stateApi.value = StateApi.LOADING
            viewModelScope.launch {
                try {
                    val response = getPexelsApi().getCuratedPhotos(currentPage, perPage)
                    pexelsPhotosLiveData.value = response.photos
                    currentPage++
                    stateApi.value = StateApi.LOADED
                } catch (e: Exception) {
                    errorLiveData.value = "Error: ${e.message}"
                }
            }
        }else{
            stateApi.value = StateApi.NO_INTERNET
        }
    }
}

