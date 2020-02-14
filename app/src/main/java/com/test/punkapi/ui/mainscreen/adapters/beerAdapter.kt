package com.test.punkapi.ui.mainscreen.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.test.punkapi.App
import com.test.punkapi.R
import com.test.punkapi.ui.mainscreen.models.BeerModel


class beerAdapter(
    val context: Context,
    val listBeers: ArrayList<BeerModel>,
    val event: OnBeerTouchEvent
) : RecyclerView.Adapter<beerAdapter.BeerItem>() {

    inner class BeerItem(private val beerView: View) : RecyclerView.ViewHolder(beerView) {
        fun bindItems(beerModel: BeerModel) {
            val constraintLayout = beerView.findViewById<ConstraintLayout>(R.id.main_beer_cv_parentLayout)
            val progressBar = beerView.findViewById<ProgressBar>(R.id.main_beer_loading_pb)
            val imageView = beerView.findViewById<ImageView>(R.id.main_beer_cv_icon)
            val textview_name = beerView.findViewById<AppCompatTextView>(R.id.main_beer_cv_tv_name)
            val textview_tagline = beerView.findViewById<AppCompatTextView>(R.id.main_beer_cv_tv_tagline)

            if (beerModel.id != 0L && beerModel.name != "Pirata") {
                constraintLayout.setBackgroundResource(R.drawable.round_corners)
                progressBar.visibility = View.GONE
                beerModel.loadImage(imageView)
                textview_name.text = beerModel.name
                textview_tagline.text = beerModel.tagline
                beerView.setOnClickListener {
                    event.onTouchItem(beerModel)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    constraintLayout.setBackgroundColor(App.instance.getColor(R.color.white))
                } else
                    constraintLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            App.instance.applicationContext,
                            R.color.white
                        )
                    )
                progressBar.visibility = View.VISIBLE
                imageView.setImageDrawable(null)
                textview_name.text = ""
                textview_tagline.text = ""
            }
        }
    }

    interface OnBeerTouchEvent {
        fun onTouchItem(beerModel: BeerModel)
    }


    fun addItemsBefore(listBeers: List<BeerModel>) {
        repeat(listBeers.size) {
            this.listBeers.add(0, listBeers[listBeers.size - it -1])
            notifyItemInserted(0)
        }
    }

    fun addItemsAfter(listBeers: List<BeerModel>) {
        var rest = if (this.listBeers.size <= 1) 0 else this.listBeers.size - 1
        repeat(listBeers.size) {
            this.listBeers.add(rest, listBeers[it])
            notifyItemInserted(rest)
            rest++
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerItem =
        BeerItem(LayoutInflater.from(context).inflate(R.layout.main_beer_cardview, parent, false))

    override fun getItemCount(): Int =
        listBeers.size

    override fun onBindViewHolder(holder: BeerItem, position: Int) =
        holder.bindItems(listBeers[position])

}