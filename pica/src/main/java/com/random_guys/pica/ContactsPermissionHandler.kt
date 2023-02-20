package com.random_guys.pica

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

interface ContactsPermissionHandler {
    fun checkAndHandleContactsPermission(componentActivity: ComponentActivity)
}

class ContactsPermissionHandlerImpl :
    ContactsPermissionHandler {

    private lateinit var componentActivity: ComponentActivity

    private val permissionLauncher: ActivityResultLauncher<String> by lazy {
        componentActivity.registerForActivityResult(ActivityResultContracts.RequestPermission())
        { }
    }

    override fun checkAndHandleContactsPermission(componentActivity: ComponentActivity) {
        this.componentActivity = componentActivity

        if (ContextCompat.checkSelfPermission(componentActivity, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

}