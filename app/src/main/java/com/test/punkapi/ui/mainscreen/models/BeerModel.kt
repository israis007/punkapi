package com.test.punkapi.ui.mainscreen.models

import android.widget.ImageView
import androidx.annotation.Keep
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson

@Keep
data class BeerModel (
    val id : Int,
    val name : String,
    val tagline : String,
    val first_brewed : String,
    val description : String,
    val image_url : String,
    val abv : Double,
    val ibu : Double,
    val target_fg : Int,
    val target_og : Double,
    val ebc : Int,
    val srm : Int,
    val ph : Double,
    val attenuation_level : Int,
    val volume : Volume,
    val boil_volume : Boil_volume,
    val method : Method,
    val ingredients : Ingredients,
    val food_pairing : List<String>,
    val brewers_tips : String,
    val contributed_by : String
){
    fun loadImage(imageView: ImageView){
        Glide.with(imageView.context)
            .setDefaultRequestOptions(RequestOptions().fitCenter())
            .load(image_url)
            .placeholder(android.R.drawable.ic_menu_search)
            .into(imageView)
    }

    override fun toString(): String = Gson().toJson(this)

}

data class Volume (
    val value: Int,
    val unit: String
)

data class Boil_volume (
    val value : Int,
    val unit : String
)

data class Temp (
    val value: Int,
    val unit: String
)

data class Mash_temp (
    val temp: Temp,
    val duration: Int
)

data class Fermentation (
    val temp: Temp
)

data class Method (
    val mash_temp : List<Mash_temp>,
    val fermentation : Fermentation,
    val twist : String
)

data class Ingredients (
    val malt : List<Malt>,
    val hops : List<Hops>,
    val yeast : String
)

data class Amount (
    val value : Double,
    val unit : String
)

data class Malt (
    val name: String,
    val amount: Amount
)

data class Hops (
    val name : String,
    val amount : Amount,
    val add : String,
    val attribute : String
)