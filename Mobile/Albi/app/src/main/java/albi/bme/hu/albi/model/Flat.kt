package albi.bme.hu.albi.model

import android.os.Parcel
import android.os.Parcelable
import android.support.v7.util.DiffUtil

class Flat(var _id: String = " ok ", var price: Int = 0, var address: String = "", var numberOfBeds: Int = 0) : Parcelable {

    constructor(parcel: Parcel) : this() {
        _id = parcel.readString()
        price = parcel.readInt()
        address = parcel.readString()
        numberOfBeds = parcel.readInt()
    }


    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(price)
        dest?.writeString(address)
        dest?.writeInt(numberOfBeds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Flat> {
        override fun createFromParcel(parcel: Parcel): Flat {
            return Flat(parcel)
        }

        override fun newArray(size: Int): Array<Flat?> {
            return arrayOfNulls(size)
        }

        var DIFF_CALLBACK: DiffUtil.ItemCallback<Flat> = object : DiffUtil.ItemCallback<Flat>() {
            override fun areItemsTheSame(oldItem: Flat, newItem: Flat): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Flat, newItem: Flat): Boolean {
                return oldItem == newItem
            }

        }

    }


    override fun equals(obj: Any?): Boolean {
        if (obj === this)
            return true

        val flat = obj as Flat?
        return flat!!._id == this._id
    }
}

