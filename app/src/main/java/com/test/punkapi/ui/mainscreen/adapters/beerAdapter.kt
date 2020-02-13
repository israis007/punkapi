package com.test.punkapi.ui.mainscreen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.test.punkapi.R
import com.test.punkapi.ui.mainscreen.models.BeerModel

class beerAdapter(
    val context: Context,
    val listBeers: List<BeerModel>,
    val event: onBeerTouchEvent
) : RecyclerView.Adapter<beerAdapter.BeerItem>() {

    inner class BeerItem(private val beerView: View) : RecyclerView.ViewHolder(beerView) {
        fun bindItems(beerModel: BeerModel) {
            beerModel.loadImage(beerView.findViewById(R.id.main_beer_cv_icon) as ImageView)
            beerView.findViewById<AppCompatTextView>(R.id.main_beer_cv_tv_name).text = beerModel.name
            beerView.findViewById<AppCompatTextView>(R.id.main_beer_cv_tv_tagline).text = beerModel.tagline
            beerView.setOnClickListener {
                event.onTouchItem(beerModel)
            }
        }
    }

    interface onBeerTouchEvent {
        fun onTouchItem(beerModel: BeerModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerItem =
        BeerItem(LayoutInflater.from(context).inflate(R.layout.main_beer_cardview, parent, false))

    override fun getItemCount(): Int =
        listBeers.size

    override fun onBindViewHolder(holder: BeerItem, position: Int) =
        holder.bindItems(listBeers[position])
}