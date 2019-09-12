package com.random_guys.pica

import android.Manifest
import android.app.Activity
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.loader.content.CursorLoader
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*
import kotlin.collections.ArrayList


class Pica(private val activity: Activity) {

    private fun checkPermissions(callback: () -> Unit) {
        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report?.isAnyPermissionPermanentlyDenied!!) {
                        val permissions = report.deniedPermissionResponses
                            .asSequence()
                            .filter { it.isPermanentlyDenied }
                            .map { it.permissionName }

                        Toast.makeText(
                            activity.baseContext,
                            "Please allow ${permissions.toList().joinToString { "," }}, it has been permanently denied",
                            LENGTH_SHORT
                        ).show()
                    }

                    if (report.areAllPermissionsGranted()) callback()
                    else {
                        val permissions = report.deniedPermissionResponses.map { it.permissionName }
                        Toast.makeText(
                            activity.baseContext,
                            "These permissions: ${permissions.joinToString { "," }}" +
                                    " have been denied. Please enabled them to continue",
                            LENGTH_SHORT
                        ).show()
                    }
                }
            }).check()
    }

    fun load(callback: (contacts: ArrayList<MainContact>) -> Unit) {
        checkPermissions {
            val contacts = ArrayList<MainContact>()
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

            val contactsMap = HashMap<String, MainContact>(cursor?.count!!)

            val contactNumberColumnIndex = cursor.getColumnIndex(Phone.NUMBER)
            val nameIndex = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)
            val numberCount = cursor.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER)

            cursor.moveToFirst().let {
                while (cursor.isAfterLast.not()) {
                    val hasNumber = cursor.getString(numberCount).toInt() > 0

                    // Contact doesn't have any number's skip it.
                    if (hasNumber.not()) cursor.moveToNext()

                    val contactDisplayName = cursor.getString(nameIndex)
                    val number = cursor.getString(contactNumberColumnIndex).formatPhoneNumber()

                    if (number.isNigerianNumber()) {
                        val contact = MainContact()
                        contact.number = number.trim()
                        contact.name = contactDisplayName.trim()
                        contact.contactType = MainContact.MainContactType.Local
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
    }

    companion object {
        private const val TAG = "Pica"
    }
}