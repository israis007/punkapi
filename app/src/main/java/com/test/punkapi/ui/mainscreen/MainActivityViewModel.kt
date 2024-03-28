package com.test.punkapi.ui.mainscreen

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.test.punkapi.App
import com.test.punkapi.R
import com.test.punkapi.rest.punkapi.RepositoryPunkAPI
import com.test.punkapi.ui.base.BaseModel
import com.test.punkapi.ui.mainscreen.models.BeerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel: BaseModel() {

    val page = MutableLiveData<Int>()
    private val elementsByPage = MutableLiveData<Int>()
    var beerRetro = MutableLiveData<List<BeerModel>>()
    var tagline = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var brewedDate = MutableLiveData<String>()
    var foodPairing = MutableLiveData<String>()
    var nameToolbar = MutableLiveData<String>()
    var imageURL = MutableLiveData<String>()

    private val TAG = "MainVM"
    private var pageAfter = App.instance.resources.getInteger(R.integer.pageBegin)

    init {
        page.value = App.instance.resources.getInteger(R.integer.pageBegin)
        elementsByPage.value = App.instance.resources.getInteger(R.integer.elementsByPage)
        tagline.value = ""
        description.value = ""
        brewedDate.value = ""
        foodPairing.value = ""
        nameToolbar.value = App.instance.getString(R.string.app_name)
    }

    lateinit var activityViewModel: ViewModelStoreOwner

    fun updatePage(isUp: Boolean){
        var pageN = page.value!!.toInt()
        if (isUp){
            if (pageAfter > 1) {
                pageAfter--
                page.value = pageAfter
            }
        } else {
            pageN++
            page.value = pageN
        }
    }

    fun getBeersRetro(){
        viewModelScope.launch(Dispatchers.IO) {
            val chelas = RepositoryPunkAPI.getBeers(
                activityViewModel,
                page.value!!.toInt(),
                elementsByPage.value!!.toInt()
            )
            beerRetro.postValue(chelas)
//            if (chelas == 44) {
////                showNoInternet.postValue(View.VISIBLE as Integer)
////                showMainLayout.postValue(View.GONE as Integer)
//                return@launch
//            } else {
////                showNoInternet.postValue(View.GONE as Integer)
////                showMainLayout.postValue(View.VISIBLE as Integer)
//                beerRetro.postValue(chelas as List<BeerModel>)
//            }
        }
    }

}