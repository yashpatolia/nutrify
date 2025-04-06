package com.example.nutrify.ui

import android.os.Parcel
import android.os.Parcelable

data class QuestionAnswer(var question: String, var answer: String, var UUID: String) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(question)
        dest.writeString(answer)
        dest.writeString(UUID)
    }

    companion object CREATOR : Parcelable.Creator<QuestionAnswer> {
        override fun createFromParcel(parcel: Parcel): QuestionAnswer {
            return QuestionAnswer(parcel)
        }

        override fun newArray(size: Int): Array<QuestionAnswer?> {
            return arrayOfNulls(size)
        }
    }

}
