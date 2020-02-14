package com.test.punkapi.ui.mainscreen

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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

        //Creating the adapter
        adapter = beerAdapter(this, listaToAdap, object: beerAdapter.OnBeerTouchEvent{
            override fun onTouchItem(beerModel: BeerModel) {
                //Update ui Main
                mainVM.imageURL.value = beerModel.image_url
                mainVM.tagline.value = beerModel.tagline
                var foodpa = ""
                beerModel.food_pairing.forEach {
                    if (!it.isNullOrEmpty())
                        foodpa += "* $it \n"
                }
                mainVM.foodPairing.value = foodpa
                mainVM.description.value = beerModel.description
                mainVM.nameToolbar.value = beerModel.name
                mainVM.brewedDate.value = beerModel.first_brewed
            }
        })
        main_recview.adapter = adapter
        main_recview.setHasFixedSize(false)
        main_recview.isNestedScrollingEnabled = true
        nonetwork_layout.visibility = View.GONE

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
        main_actv_description_text.movementMethod = ScrollingMovementMethod()
        main_actv_food_text.movementMethod = ScrollingMovementMethod()
        nonetwork_layout.setOnClickListener {
            refreshData(false)
        }

        //Observers
        mainVM.beerRetro.observe(this, Observer {
            loadRecyclerView(it)
        })
        mainVM.nameToolbar.observe(this, Observer {
            this.title = mainVM.nameToolbar.value.toString()
        })
        mainVM.imageURL.observe(this, Observer{
            Glide.with(this)
                .setDefaultRequestOptions(RequestOptions().fitCenter())
                .load(mainVM.imageURL.value.toString())
                .placeholder(android.R.drawable.ic_menu_search)
                .into(main_iv)
        })

        //Get first Data
        mainVM.showNoInternet.value = View.GONE as Integer
        mainVM.showMainLayout.value = View.VISIBLE as Integer
        refreshData(false)
    }

    private fun refreshData(isUp: Boolean){
        nonetwork_layout.visibility = View.GONE
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
}
