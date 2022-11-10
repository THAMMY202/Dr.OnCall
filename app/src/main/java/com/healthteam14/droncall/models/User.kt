package com.healthteam14.droncall.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var email: String,
    var firstName: String?,
    var lastName: String?,
    var phoneNumber: String?,
    var role: String,
    var key: String,
    var gender: String,
    var birthYear: String,
) : Parcelable {
    constructor() : this("", "", "", "", "", "","","")
}