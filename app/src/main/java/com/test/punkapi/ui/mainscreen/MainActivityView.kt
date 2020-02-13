package com.test.punkapi.ui.mainscreen

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.punkapi.App
import com.test.punkapi.R
import com.test.punkapi.databinding.ActivityMainBinding
import com.test.punkapi.ui.mainscreen.adapters.beerAdapter
import com.test.punkapi.ui.mainscreen.models.BeerModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityView : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mainVM: MainActivityViewModel
    private val TAG = "MainActivityView"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainVM = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding.mainActivityViewModel = mainVM
        mainVM.activityViewModel = this
        binding.lifecycleOwner = this
        mainVM.page.value = 1
        mainVM.elementsByPage.value = App.instance.resources.getInteger(R.integer.elementsByPage)
        mainVM.beers.observe(this@MainActivityView, Observer {
            loadRecyclerView(it)
        })
//        mainVM.setup()
    }

    private fun loadRecyclerView(beers: List<BeerModel>){
        main_recview.layoutManager = GridLayoutManager(this@MainActivityView, 1)
        main_recview.adapter = beerAdapter(this, beers, object: beerAdapter.onBeerTouchEvent{
            override fun onTouchItem(beerModel: BeerModel) {
                //Update ui Main
                Log.d("Main", beerModel.toString())
            }
        })
        main_recview.setHasFixedSize(false)
        main_recview.isNestedScrollingEnabled = true
        main_recview.adapter!!.notifyDataSetChanged()
    }
}
