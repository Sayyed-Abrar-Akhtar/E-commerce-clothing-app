package com.sayyed.onlineclothingapplication.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Product(

    var productTitle: String? = null,
    var productPrice: String? = null,
    var productDescription: String? = null,
    var productColor: String? = null,
    var productSize: String? = null,
    var productImage: String? = null

) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var productId: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productTitle)
        parcel.writeString(productPrice)
        parcel.writeString(productDescription)
        parcel.writeString(productColor)
        parcel.writeString(productSize)
        parcel.writeString(productImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}