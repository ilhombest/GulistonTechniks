package com.ahmedelsayed.sunmiprinterapp.recyclerview

import android.os.Parcel
import android.os.Parcelable

data class SearchViewModel(val name: String, val address: String, val balance:String, val tvId:String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(balance)
        parcel.writeString(tvId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SearchViewModel> {
        override fun createFromParcel(parcel: Parcel): SearchViewModel {
            return SearchViewModel(parcel)
        }

        override fun newArray(size: Int): Array<SearchViewModel?> {
            return arrayOfNulls(size)
        }
    }
}