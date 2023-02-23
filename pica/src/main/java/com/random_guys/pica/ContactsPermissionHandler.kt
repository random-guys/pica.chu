package com.random_guys.pica

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

interface ContactsPermissionHandler {

    val permission: String
        get() = Manifest.permission.READ_CONTACTS

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

    val shouldShowRequestPermissionRationale: Boolean

    fun checkAndHandleContactsPermission(
        educationUiCallback: ((() -> Unit) -> Unit)? = null,
        onGrantedCallback: () -> Unit,
        onDenyCallback: () -> Unit
    )
}

class ContactsPermissionHandlerImpl :
    ContactsPermissionHandler {

    private lateinit var componentActivity: ComponentActivity
    private lateinit var onGrantedCallback: () -> Unit
    private lateinit var onDenyCallBack: () -> Unit

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun initContactsPermissionHandler(activity: ComponentActivity) {
        this.componentActivity = activity
        this.permissionLauncher =
            componentActivity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) onGrantedCallback() else onDenyCallBack()
            }
    }

    override val hasContactsPermission: Boolean
        get() = (ContextCompat.checkSelfPermission(
            componentActivity, permission
        ) == PackageManager.PERMISSION_GRANTED)

    override val shouldShowRequestPermissionRationale: Boolean
        get() = ActivityCompat.shouldShowRequestPermissionRationale(
            componentActivity, permission
        )

    override fun checkAndHandleContactsPermission(
        educationUiCallback: ((() -> Unit) -> Unit)?,
        onGrantedCallback: () -> Unit,
        onDenyCallback: () -> Unit
    ) {
        this.onGrantedCallback = onGrantedCallback
        this.onDenyCallBack = onDenyCallback

        if (hasContactsPermission) {
            onGrantedCallback()
        } else {
            permissionLauncher.launch(permission)
        }

        when {
            hasContactsPermission -> {
                onGrantedCallback()
            }
            shouldShowRequestPermissionRationale -> {
                if (educationUiCallback == null) permissionLauncher.launch(permission)
                else educationUiCallback { permissionLauncher.launch(permission) }
            }
            else -> {
                permissionLauncher.launch(permission)
            }
        }
    }
}