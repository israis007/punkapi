package com.test.punkapi.ui.base.models

data class NotServiceResponse(
    val code: Int,
    val body: String,
    val error: String
) {
    fun serverIsBroke(): Boolean = code > 500
}