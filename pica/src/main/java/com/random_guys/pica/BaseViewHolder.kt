package com.random_guys.pica

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by rtukpe on 14/03/2018.
 */

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    @JvmOverloads
    open fun onBind(position: Int = adapterPosition) {

    }
}