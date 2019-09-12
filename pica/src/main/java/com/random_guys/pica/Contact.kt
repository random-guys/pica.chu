package com.random_guys.pica

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import de.hdodenhof.circleimageview.CircleImageView

open class Contact() : AbstractItem<Contact.ViewHolder>(), Parcelable {

    lateinit var name: String
    lateinit var number: String
    lateinit var contactType: ContactType

    override val layoutRes: Int = R.layout.contact_item

    override val type: Int = R.id.contact

    constructor(parcel: Parcel) : this()

    enum class ContactType { Server, Local }

    override fun writeToParcel(parcel: Parcel, flags: Int) = Unit

    override fun describeContents(): Int = 0

    override fun toString(): String = "$number - $name"

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    open class ViewHolder(itemView: View) : FastAdapter.ViewHolder<Contact>(itemView) {
        private val iconView: TitleLabelIconView = itemView.findViewById(R.id.contact)
        private val logoImageView = itemView.findViewById<CircleImageView>(R.id.logo_icon)

        override fun bindView(item: Contact, payloads: MutableList<Any>) {
            iconView.title = item.name
            iconView.label = item.number.trim()
            iconView.initials = item.name.initials()
            logoImageView.visibility =
                if (item.contactType == ContactType.Local) View.INVISIBLE else View.VISIBLE
        }

        override fun unbindView(item: Contact) {}
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}