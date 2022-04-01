package com.techpig.rogys.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
    val user_id: String = "",
    val productId: String = "",
    val title: String = "",
    val price: String = "",
    val image: String = "",
    var car_quantity: String = "",
    var stock_quantity: String = "",
    var id: String = ""
) : Parcelable