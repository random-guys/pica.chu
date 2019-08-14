package com.random_guys.pica

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class TitleLabelIconView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val mTitleTextView: TextView
    private val mLabelTextView: TextView
    private val mIconImageView: ImageView
    private val mInitialsTextView: TextView


    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TitleLabelIconView, 0, 0)
        val color = a.getColor(R.styleable.TitleLabelIconView_textColor, ContextCompat.getColor(context, R.color.black))
        val drawable = a.getDrawable(R.styleable.TitleLabelIconView_drawable)
        val initials = a.getString(R.styleable.TitleLabelIconView_initials)
        val title = a.getString(R.styleable.TitleLabelIconView_title)
        val label = a.getString(R.styleable.TitleLabelIconView_label)

        LayoutInflater.from(context).inflate(R.layout.title_label_icon_view, this, true)
        val root = this[0] as ConstraintLayout

        mIconImageView = root[0] as ImageView
        mIconImageView.setImageDrawable(drawable)

        mInitialsTextView = root[1] as TextView
        mInitialsTextView.text = initials

        mTitleTextView = root[2] as TextView
        mTitleTextView.text = title
        mTitleTextView.setTextColor(color)

        mLabelTextView = root[3] as TextView
        mLabelTextView.text = label

        a.recycle()
    }

    var initials: String
        get() = mInitialsTextView.text.toString()
        set(value) {
            mInitialsTextView.text = value
        }

    var title: String
        get() = mTitleTextView.text.toString()
        set(value) {
            mTitleTextView.text = value
        }

    var label
        get() = mLabelTextView.text.toString()
        set(value) {
            mLabelTextView.text = value
        }

    private operator fun ViewGroup.get(position: Int): View = getChildAt(position)
}