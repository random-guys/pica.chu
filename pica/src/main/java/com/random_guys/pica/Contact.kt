package com.random_guys.pica

import android.os.Parcel
import android.os.Parcelable
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion

class Contact : SearchSuggestion {

    override fun getBody(): String = name.toLowerCase()

    var name: String = ""
    var id: String = ""

    var emails: ArrayList<ContactEmail> = ArrayList()
    var numbers: ArrayList<ContactPhone> = ArrayList()

    override fun toString(): String {
        var result = "$id - $name"
        if (numbers.size > 0) {
            val number = numbers[0]
            result += " (" + number.number + " - " + number.type + ")"
        }
        if (emails.size > 0) {
            val (address, type) = emails[0]
            result += " [$address - $type]"
        }
        return result
    }

    fun addEmail(address: String, type: String) {
        emails.add(ContactEmail(address, type))
    }

    fun addNumber(number: String, type: String) {
        numbers.add(ContactPhone(number, type))
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact = Contact()
        override fun newArray(size: Int): Array<Contact?> = arrayOfNulls(size)
    }
}