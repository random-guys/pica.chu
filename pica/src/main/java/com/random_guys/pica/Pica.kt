package com.random_guys.pica

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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
                    if (report.areAllPermissionsGranted()) {
                        callback()
                    } else {
                        val permissions = report.deniedPermissionResponses.map { it.permissionName }
                        Toast.makeText(
                            activity.baseContext,
                            "These permissions: ${permissions.joinToString { "," }}" +
                                    " have been denied. Please enabled them to continue", LENGTH_SHORT
                        ).show()
                    }
                }
            }).check()
    }

    fun chu(callback: (contacts: ArrayList<Contact>) -> Unit) {
        checkPermissions {
            val contacts = ArrayList<Contact>()
            val contentResolver: ContentResolver = activity.baseContext.contentResolver
            val projectionFields = arrayOf(
                ContactsContract.Data.HAS_PHONE_NUMBER,
                Phone.DISPLAY_NAME,
                Phone.NUMBER,
                Phone.TYPE
            )

            val cursor: Cursor? = contentResolver.query(
                Phone.CONTENT_URI,
                projectionFields, // projection fields
                null, // the selection criteria
                null, // the selection args
                Phone.DISPLAY_NAME + " ASC" // the sort order
            )

            if (cursor == null || cursor.count <= 0) callback(ArrayList())

            val contactsMap = HashMap<String, Contact>(cursor?.count!!)

            val contactTypeColumnIndex = cursor.getColumnIndex(Phone.TYPE)
            val contactNumberColumnIndex = cursor.getColumnIndex(Phone.NUMBER)
            val nameIndex = cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)
            val numberCount = cursor.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER)


            cursor.moveToFirst().let {
                while (cursor.isAfterLast.not()) {
                    val hasNumber = cursor.getString(numberCount).toInt() > 0

                    // Contact doesn't have any number's skip it.
                    if (hasNumber.not()) cursor.moveToNext()

                    val type = cursor.getInt(contactTypeColumnIndex)
                    val contactDisplayName = cursor.getString(nameIndex)
                    val number = cursor.getString(contactNumberColumnIndex).formatPhoneNumber()

                    val customLabel = "Custom"
                    val phoneType = Phone.getTypeLabel(activity.baseContext.resources, type, customLabel)

                    if (number.isNigerianNumber()) {
                        if (contactsMap[number] == null) {
                            val contact = contactDisplayName?.let { Contact(number, it) }
                            contact?.addNumber(number, phoneType.toString())
                            contact?.let { contactsMap[number] = it }
                        } else {
                            contactsMap[number]?.addNumber(number, phoneType.toString())
                        }
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