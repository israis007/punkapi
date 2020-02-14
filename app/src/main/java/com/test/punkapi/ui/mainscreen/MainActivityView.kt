package com.test.punkapi.ui.mainscreen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.punkapi.R
import com.test.punkapi.databinding.ActivityMainBinding
import com.test.punkapi.ui.mainscreen.adapters.beerAdapter
import com.test.punkapi.ui.mainscreen.models.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityView : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mainVM: MainActivityViewModel
    private val TAG = "MainActivityView"
    val linearlm = LinearLayoutManager(this)
    lateinit var adapter: beerAdapter
    private var firstTime = true
    private var isUp = false
    private var isScrolling = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainVM = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding.mainActivityViewModel = mainVM
        mainVM.activityViewModel = this
        binding.lifecycleOwner = this
        //Creation of LayoutManager
        linearlm.orientation = RecyclerView.VERTICAL
        main_recview.layoutManager = linearlm
        val listaToAdap = ArrayList<BeerModel>()
        //Adding a clean item to show loading circle
        listaToAdap.add(BeerModel(0, "Pirata","tagline", "", "", "", 0.0, 0.0, 0.0,0.0, 0.0, 0.0, 0.0, 0.0, Volume(0.0, ""), Boil_volume(0.0, ""),
            Method(List(1){ Mash_temp(Temp(0.0, ""), 1.0)}, Fermentation(Temp(
            Double.MIN_VALUE, "")), ""),
            Ingredients(List(1){ Malt("", Amount(0.0, ""))},
                List(1){ Hops("", Amount(0.0, ""), "", "")},
                ""),
            List(1){""}, "", ""
        ))
        adapter = beerAdapter(this, listaToAdap, object: beerAdapter.OnBeerTouchEvent{
            override fun onTouchItem(beerModel: BeerModel) {
                //Update ui Main
                Log.d("Main", beerModel.toString())
            }
        })
        main_recview.adapter = adapter
        main_recview.setHasFixedSize(false)
        main_recview.isNestedScrollingEnabled = true
        //Events
        main_swiperl.setOnRefreshListener {
            refreshData(true)
        }
        main_recview.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isScrolling && (linearlm.itemCount - linearlm.findFirstVisibleItemPosition() - linearlm.childCount == 0)){
                    isScrolling = false
                    refreshData(false)
                }
                if (firstTime){
                    firstTime = false
                    linearlm.scrollToPosition(0)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }
        })
        //Observers
        mainVM.beerRetro.observe(this, Observer {
            loadRecyclerView(it)
        })

        //Get first Data
        refreshData(false)
    }

    private fun refreshData(isUp: Boolean){
        this.isUp = isUp
        if (mainVM.page.value!!.toInt() == 1)
            main_swiperl.isRefreshing = false
        else {
            mainVM.updatePage(isUp)
            mainVM.getBeersRetro()
        }
    }

    private fun loadRecyclerView(beers: List<BeerModel>){
        if (isUp) {
            adapter.addItemsBefore(beers)
            linearlm.scrollToPosition(0)
        } else
            adapter.addItemsAfter(beers)
        mainVM.busy.value = View.GONE as Integer
        main_swiperl.isRefreshing = false
    }

    private fun loadRecyclerView(beers: BeerModel){
        if (isUp)
            adapter.addItemsBefore(beers)
        else
            adapter.addItemsAfter(beers)
        mainVM.busy.value = View.GONE as Integer
        main_swiperl.isRefreshing = false
    }
}
