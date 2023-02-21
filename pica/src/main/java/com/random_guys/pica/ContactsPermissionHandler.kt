package com.random_guys.pica

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

interface ContactsPermissionHandler {
    /**
     * Call this function in onCreate
     */
    fun initContactsPermissionHandler(activity: ComponentActivity)

    /**
     * Returns true if the user has allowed contacts permission.
     * You can use this to show a dialog explaining the reason for
     * requesting permission before actually requesting the permission.
     */
    val hasContactsPermission: Boolean

    fun checkAndHandleContactsPermission(onGrantedCallback: () -> Unit)
}

class ContactsPermissionHandlerImpl :
    ContactsPermissionHandler {

    private lateinit var componentActivity: ComponentActivity
    private lateinit var onGrantedCallback: () -> Unit

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun initContactsPermissionHandler(activity: ComponentActivity) {
        this.componentActivity = activity
        this.permissionLauncher =
            componentActivity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) onGrantedCallback()
            }
    }

    override val hasContactsPermission: Boolean
        get() = (ContextCompat.checkSelfPermission(
            componentActivity, Manifest.permission.READ_CONTACTS
        ) != PackageManager.PERMISSION_GRANTED)

    override fun checkAndHandleContactsPermission(onGrantedCallback: () -> Unit) {
        this.onGrantedCallback = onGrantedCallback

        if (hasContactsPermission) {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        } else {
            onGrantedCallback()
        }
    }

}