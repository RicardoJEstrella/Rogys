package com.techpig.rogys.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0L,
    val gender: String = "",
    val profileCompleted: Int = 0) : Parcelable