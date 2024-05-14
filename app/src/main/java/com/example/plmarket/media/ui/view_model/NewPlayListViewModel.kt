package com.example.plmarket.media.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plmarket.media.domain.db.PlayListInteractor
import com.example.plmarket.media.ui.CreateNewPlayListState
import kotlinx.coroutines.launch

class NewPlayListViewModel(private val interactor: PlayListInteractor) : ViewModel() {

    private val stateNewAndCreatePlayList = MutableLiveData<CreateNewPlayListState>()
    fun observeStateNewAndCreatePlayList(): LiveData<CreateNewPlayListState> =
        stateNewAndCreatePlayList

    fun checkStateCreateAndNew(id: Int) {
        if (id == 0) {
            stateNewAndCreatePlayList.postValue(CreateNewPlayListState.NewPlayList)
        } else {
            viewModelScope.launch {
                val playList = interactor.getPlayList(id)
                stateNewAndCreatePlayList.postValue(
                    CreateNewPlayListState.CreatePlayList(playList)
                )
            }
        }
    }

    fun updatePlayList(name: String, description: String, uri: String, id: Int) {
        viewModelScope.launch {
            interactor.updatePlayList(name, description, uri, id)
        }
    }

    suspend fun addPlayList(name: String, description: String, uri: String) {
        interactor.addPlayList(name, description, uri)
    }

    fun addImgToStorage(uri: Uri) {
        interactor.saveImageToPrivateStorage(uri)
    }

    fun getUri(uriPlayList: String): String {
        return interactor.getUri(uriPlayList)
    }


}