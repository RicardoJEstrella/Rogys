package com.techpig.rogys.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val user_id: String = "",
    val user_name: String = "",
    val title: String = "",
    val price: String = "",
    val description: String = "",
    val image: String = "",
    var product_id: String = "",
    var stock_quantity: String = ""
) : Parcelable
