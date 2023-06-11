package com.ahmedelsayed.sunmiprinterapp.history

import android.os.Parcel
import android.os.Parcelable
import com.ahmedelsayed.sunmiprinterapp.recyclerview.SearchViewModel

data class HistoryViewModel(val summa: String, val date: String, val prinyal:String, val comment:String):
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(summa)
        parcel.writeString(date)
        parcel.writeString(prinyal)
        parcel.writeString(comment)
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
