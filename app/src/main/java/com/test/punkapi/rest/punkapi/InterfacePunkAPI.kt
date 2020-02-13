package com.test.punkapi.rest.punkapi

import com.test.punkapi.ui.mainscreen.models.BeerModel
import retrofit2.http.GET
import retrofit2.http.Query

interface InterfacePunkAPI {

    @GET("/v2/beers")
    suspend fun getBeers(@Query("page") page: Int, @Query("per_page") elementsByPage: Int): List<BeerModel>
}