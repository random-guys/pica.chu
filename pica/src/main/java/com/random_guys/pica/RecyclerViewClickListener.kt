package com.random_guys.pica

import android.view.View

interface RecyclerViewClickListener {
    fun recyclerViewItemClicked(v: View, position: Int)
}