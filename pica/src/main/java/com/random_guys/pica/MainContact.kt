package com.random_guys.pica

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import de.hdodenhof.circleimageview.CircleImageView

open class MainContact() : AbstractItem<MainContact.ViewHolder>(), Parcelable {

    lateinit var name: String
    lateinit var number: String
    lateinit var contactType: MainContactType

    override val layoutRes: Int = R.layout.contact_item

    override val type: Int = R.id.contact

    constructor(parcel: Parcel) : this()

    enum class MainContactType { Server, Local }

    override fun writeToParcel(parcel: Parcel, flags: Int) = Unit

    override fun describeContents(): Int = 0

    override fun toString(): String = "$number - $name"

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    open class ViewHolder(itemView: View) : FastAdapter.ViewHolder<MainContact>(itemView) {
        private val iconView: TitleLabelIconView = itemView.findViewById(R.id.contact)
        private val logoImageView = itemView.findViewById<CircleImageView>(R.id.logo_icon)

        override fun bindView(item: MainContact, payloads: MutableList<Any>) {
            iconView.title = item.name
            iconView.label = item.number.trim()
            iconView.initials = item.name.initials()
            logoImageView.visibility =
                if (item.contactType == MainContactType.Local) View.INVISIBLE else View.VISIBLE
        }

        override fun unbindView(item: MainContact) {}
    }

    companion object CREATOR : Parcelable.Creator<MainContact> {
        override fun createFromParcel(parcel: Parcel): MainContact {
            return MainContact(parcel)
        }

        override fun newArray(size: Int): Array<MainContact?> {
            return arrayOfNulls(size)
        }
    }
}