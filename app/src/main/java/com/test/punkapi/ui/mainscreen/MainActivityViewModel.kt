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
    var busy = MutableLiveData<Integer>()
    private val TAG = "MainVM"
    private var pageAfter = App.instance.resources.getInteger(R.integer.pageBegin)

    init {
        busy.value = View.VISIBLE as Integer
        page.value = App.instance.resources.getInteger(R.integer.pageBegin)
        elementsByPage.value = App.instance.resources.getInteger(R.integer.elementsByPage)
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
        busy.value = View.VISIBLE as Integer
        viewModelScope.launch(Dispatchers.IO) {
            beerRetro.postValue(RepositoryPunkAPI.getBeers(
                activityViewModel,
                page.value!!.toInt(),
                elementsByPage.value!!.toInt()
            ))
        }
    }

}