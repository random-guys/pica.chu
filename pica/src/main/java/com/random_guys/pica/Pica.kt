package com.random_guys.pica

import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import androidx.activity.ComponentActivity
import androidx.core.database.getStringOrNull
import androidx.loader.content.CursorLoader


class Pica(private val activity: ComponentActivity) {

    fun load(callback: (contacts: ArrayList<Contact>) -> Unit) {
        val contacts = ArrayList<Contact>()
        val projectionFields = arrayOf(
            ContactsContract.Data.HAS_PHONE_NUMBER,
            Phone.DISPLAY_NAME,
            Phone.NUMBER,
            Phone.TYPE
        )

        val cursor: Cursor? = CursorLoader(
            activity.baseContext,
            Phone.CONTENT_URI,
            projectionFields, // projection fields
            null, // the selection criteria
            null, // the selection args
            Phone.DISPLAY_NAME + " ASC" // the sort order
        ).loadInBackground()

        if (cursor == null || cursor.count <= 0) callback(ArrayList())

        val contactsMap = HashMap<String, Contact>(cursor?.count!!)

        val contactNumberColumnIndex = cursor.getColumnIndex(Phone.NUMBER)
        val nameIndex = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)
        val numberCount = cursor.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER)

        cursor.moveToFirst().let {
            while (cursor.isAfterLast.not()) {
                val hasNumber = cursor.getString(numberCount).toInt() > 0

                // Contact doesn't have any number's skip it.
                if (hasNumber.not()) cursor.moveToNext()

                val contactDisplayName = cursor.getString(nameIndex)
                val number =
                    cursor.getStringOrNull(contactNumberColumnIndex)?.formatPhoneNumber().orEmpty()

                if (number.isNigerianNumber()) {
                    val contact = Contact()
                    contact.number = number.trim()
                    contact.name = contactDisplayName.trim()
                    contact.contactType = Contact.ContactType.Local
                    contactsMap[number] = contact
                }

                cursor.moveToNext()
            }
            cursor.close()
        }

        contactsMap.values.forEach { contacts.add(it) }
        contacts.sortBy { it.name }

        callback(contacts)
    }

    companion object {
        private const val TAG = "Pica"
    }
}