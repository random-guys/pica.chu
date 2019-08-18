package com.random_guys.pica

import android.view.View
import androidx.annotation.IdRes

fun String.initials(): String {
    if (this.isEmpty()) return ""

    val parts = this.trim().split(" ")
    return when {
        parts.size > 1 -> "${parts[0][0]}${parts[1][0]}".toUpperCase()
        else -> "${parts[0][0]}".toUpperCase()
    }
}

fun String.isNigerianNumber(): Boolean {
    return this.matches(Regex("[0-9]{11}"))
}

fun String.formatPhoneNumber(): String {
    return this.trim()
        .replace(" ", "")
        .replace("+234", "0")
}

fun <V : View> View.bind(@IdRes resource: Int): V {
    val lazyValue: Lazy<V> = lazy { findViewById<V>(resource)!! }
    return lazyValue.value
}