package com.test.punkapi.ui.mainscreen

import androidx.annotation.Keep
import androidx.lifecycle.*
import com.test.punkapi.ui.base.BaseModel
import com.test.punkapi.rest.punkapi.RepositoryPunkAPI
import com.test.punkapi.ui.mainscreen.models.BeerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel: BaseModel() {

    val page = MutableLiveData<Int>()
    val elementsByPage = MutableLiveData<Int>()
    lateinit var activityViewModel: ViewModelStoreOwner
//    val beers = MutableLiveData<List<BeerModel>>()
//
//    fun setup(){
//        viewModelScope.launch(Dispatchers.IO) {
//            beers.postValue(RepositoryPunkAPI.getBeers(activityViewModel, page.value!!.toInt(), elementsByPage.value!!.toInt()))
//        }
//    }

    @Keep
    val beers: LiveData<List<BeerModel>> = liveData(Dispatchers.IO){
        val retrievedData = RepositoryPunkAPI.getBeers(activityViewModel, page.value!!.toInt(), elementsByPage.value!!.toInt())
        emit(retrievedData)
    }


}