package com.random_guys.pica

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import de.hdodenhof.circleimageview.CircleImageView


class Contact() : AbstractItem<Contact.ViewHolder>(), Parcelable, Comparable<Contact> {

    lateinit var name: String
    lateinit var number: String
    lateinit var contactType: ContactType
    var profilePicture: String = ""
    var bankCode: String = ""

    override val layoutRes: Int = R.layout.contact_item

    override val type: Int = R.id.contact

    constructor(parcel: Parcel) : this()

    enum class ContactType { Server, Local }

    override fun writeToParcel(parcel: Parcel, flags: Int) = Unit

    override fun describeContents(): Int = 0

    override fun toString(): String = "$number - $name"

    override fun getViewHolder(v: View): ViewHolder = ViewHolder(v)

    override fun compareTo(other: Contact): Int = if (contactType == ContactType.Server) -1 else 1

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<Contact>(itemView) {
        private val iconView: TitleLabelIconView = itemView.findViewById(R.id.contact)
        private val logoImageView = itemView.findViewById<CircleImageView>(R.id.logo_icon)

        override fun bindView(item: Contact, payloads: MutableList<Any>) {
            iconView.title = item.name
            iconView.label = item.number.trim()
            iconView.initials = item.name.initials()
            logoImageView.visibility =
                if (item.contactType == ContactType.Local) View.INVISIBLE else View.VISIBLE

            item.profilePicture = "https://res.cloudinary.com/gomoney/image/upload/v1585223385/banks/access.png"
            if (item.profilePicture.isNotEmpty()) {
                var requestOptions = RequestOptions()
                requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(10)).centerInside()
                Glide.with(itemView.context).load(item.profilePicture).apply(requestOptions).into(iconView.mIconImageView)
                iconView.initials = ""
                iconView.mIconImageView.alpha = 1f
            } else {
                iconView.initials = item.name.initials()
                iconView.mIconImageView.alpha = 0.3f
                when {
                    item.name.initials().contains(Regex("[A-B]")) -> iconView.mIconImageView.setImageDrawable(
                        ContextCompat.getDrawable(itemView.context, R.drawable.transaction_initials))
                    item.name.initials().contains(Regex("[C-H]")) -> iconView.mIconImageView.setImageDrawable(
                        ContextCompat.getDrawable(itemView.context, R.drawable.transaction_initials_peachy_pink))
                    else -> iconView.mIconImageView.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.transaction_initials_carolina_blue))
                }
            }
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