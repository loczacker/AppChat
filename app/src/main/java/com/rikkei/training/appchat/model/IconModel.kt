package com.rikkei.training.appchat.model

import android.os.Parcel
import android.os.Parcelable

data class IconModel(
    val iconSource: Int,
    val iconName: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    ) {
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(iconSource)
        parcel.writeString(iconName)
    }

    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<IconModel> {
        override fun createFromParcel(parcel: Parcel): IconModel {
            return IconModel(parcel)
        }
        override fun newArray(size: Int): Array<IconModel?> {
            return arrayOfNulls(size)
        }
    }

}