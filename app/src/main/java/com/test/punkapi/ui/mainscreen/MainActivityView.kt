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
    private var isScrolling = false
    private var currentItems = 0
    private var totalItems = 0
    private var scrollOutItems = 0
    private var beforePage = 6
    private val pageStart = 6
    private var lastpage = 6
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainVM = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding.mainActivityViewModel = mainVM
        mainVM.activityViewModel = this
        binding.lifecycleOwner = this
        main_swiperl.setOnRefreshListener {
            val page = mainVM.page.value!!.toInt()
            if (page in 2..pageStart){
                refreshData(true)
            } else
                main_swiperl.isRefreshing = false
        }
        val linearlm = LinearLayoutManager(this)
        linearlm.orientation = RecyclerView.VERTICAL
        main_recview.layoutManager = linearlm
        main_recview.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = linearlm.childCount
                totalItems = linearlm.itemCount
                scrollOutItems = linearlm.findFirstVisibleItemPosition()

                if (isScrolling && (currentItems + scrollOutItems == totalItems)){
                    isScrolling = false
                    refreshData(false)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    isScrolling = true
            }
        })
        mainVM.page.value = pageStart
        mainVM.elementsByPage.value = App.instance.resources.getInteger(R.integer.elementsByPage)
        refreshData(false)
    }

    private fun refreshData(isUp: Boolean){
        if (isUp){
            beforePage--
            mainVM.page.value = beforePage
        } else {
            mainVM.page.value = lastpage
            lastpage++
        }
        mainVM.beersList.removeObservers(this)
        mainVM.busy.value = View.VISIBLE as Integer
        val temp = mainVM.updateBeers(isUp)
        mainVM.beersList.observe(this, Observer {
            loadRecyclerView(it, temp, isUp)
            main_swiperl.isRefreshing = false
        })
    }

    private fun loadRecyclerView(beers: ArrayList<BeerModel>, itemsAdded: Int, isUp: Boolean){
        main_recview.adapter = beerAdapter(this, beers, object: beerAdapter.onBeerTouchEvent{
            override fun onTouchItem(beerModel: BeerModel) {
                //Update ui Main
                Log.d("Main", "La chela seleccionada es ($beerModel.name):\n$beerModel")
            }
        })
        main_recview.setHasFixedSize(false)
        main_recview.isNestedScrollingEnabled = true
        var resta = beers.size - itemsAdded
        repeat(itemsAdded) {
            if (isUp){
                main_recview.adapter!!.notifyItemInserted(it)
            } else {
                main_recview.adapter!!.notifyItemInserted(resta)
                resta++
            }
        }
        mainVM.busy.value = View.GONE as Integer
    }
}
