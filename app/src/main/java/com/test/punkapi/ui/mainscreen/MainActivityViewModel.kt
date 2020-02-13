package com.test.punkapi.ui.mainscreen

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import com.test.punkapi.rest.punkapi.RepositoryPunkAPI
import com.test.punkapi.ui.base.BaseModel
import com.test.punkapi.ui.mainscreen.models.BeerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel: BaseModel() {

    val page = MutableLiveData<Int>()
    val elementsByPage = MutableLiveData<Int>()
    lateinit var activityViewModel: ViewModelStoreOwner
    val beersList = MutableLiveData<ArrayList<BeerModel>>()
    lateinit var beers: List<BeerModel>
    private var temp = 0
    var busy: MutableLiveData<Integer> = MutableLiveData()

    init {
        busy.value = View.GONE as Integer
    }

    fun getBussy() = busy

    fun netxPage(pageNext: Int){
        var pa = page.value!!.toInt()
        if (pageNext > 1)
            pa+= pageNext
        page.value = pa
    }

    fun updateBeers(isUp: Boolean): Int {
        temp = 0
        if (beersList.value == null)
            beersList.value = ArrayList()

        viewModelScope.launch(Dispatchers.IO) {
            beers = RepositoryPunkAPI.getBeers(activityViewModel, page.value!!.toInt(), elementsByPage.value!!.toInt())
            beers.forEach { element ->
                run {
                    if (isUp)
                        beersList.value!!.add(temp, element)
                    else
                        beersList.value!!.add(element)
                    temp++
                }
            }
        }
        return temp
    }



}