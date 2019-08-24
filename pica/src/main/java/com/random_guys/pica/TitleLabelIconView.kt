package com.random_guys.pica

import android.content.Context
import android.graphics.drawable.Drawable
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
        val titleColor =
            a.getColor(R.styleable.TitleLabelIconView_pica_title_color, ContextCompat.getColor(context, R.color.black))
        val labelColor =
            a.getColor(R.styleable.TitleLabelIconView_pica_label_color, ContextCompat.getColor(context, R.color.black))
        val drawable = a.getDrawable(R.styleable.TitleLabelIconView_pica_drawable)
        val initials = a.getString(R.styleable.TitleLabelIconView_pica_initials)
        val title = a.getString(R.styleable.TitleLabelIconView_pica_title)
        val label = a.getString(R.styleable.TitleLabelIconView_pica_label)

        LayoutInflater.from(context).inflate(R.layout.title_label_icon_view, this, true)
        val root = this as ConstraintLayout

        mIconImageView = root.findViewById(R.id.icon) as ImageView
        mIconImageView.setImageDrawable(drawable)

        mInitialsTextView = root.findViewById(R.id.initials) as TextView
        mInitialsTextView.text = initials

        mTitleTextView = root.findViewById(R.id.title) as TextView
        mTitleTextView.text = title
        mTitleTextView.setTextColor(titleColor)

        mLabelTextView = root.findViewById(R.id.label) as TextView
        mLabelTextView.text = label
        mLabelTextView.setTextColor(labelColor)

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
    var drawable: Drawable
        get() = mIconImageView.drawable
        set(value) {
            mIconImageView.setImageDrawable(value)
    }

    private operator fun ViewGroup.get(position: Int): View = getChildAt(position)
}