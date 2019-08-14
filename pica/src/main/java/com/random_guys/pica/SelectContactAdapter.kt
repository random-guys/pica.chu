package com.random_guys.pica

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SelectContactAdapter(private val mContext: Context) : RecyclerView.Adapter<BaseViewHolder>() {

    private val contacts: ArrayList<Contact> = ArrayList()
    lateinit var mRecyclerViewClickListener: RecyclerViewClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view: View?
        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent, false)
                ViewHolder(view, mRecyclerViewClickListener)
            }
            else -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_empty_view, parent, false)
                EmptyViewHolder(view)
            }
        }
    }

    fun get(position: Int): Contact = contacts[position]

    fun add(item: Contact) {
        contacts.add(item)
        notifyDataSetChanged()
    }

    fun add(contacts: Collection<Contact>) {
        this.contacts.addAll(contacts)
        notifyDataSetChanged()
    }

    fun remove(index: Int) {
        contacts.removeAt(index)
        notifyDataSetChanged()
    }

    fun clear() {
        contacts.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (contacts.size > 0) {
            VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_EMPTY
        }
    }

    override fun getItemCount(): Int {
        return if (contacts.size > 0) {
            contacts.size
        } else {
            1
        }
    }

    inner class ViewHolder(itemView: View, private var recyclerViewClickListener: RecyclerViewClickListener) :
        BaseViewHolder(itemView) {

        private val contactTitleLabelIconView: TitleLabelIconView = itemView.findViewById(R.id.contact)

        override fun onBind(position: Int) {
            super.onBind(adapterPosition)

            with(contacts[adapterPosition]) {
                contactTitleLabelIconView.title = name
                contactTitleLabelIconView.initials = name.initials()
                contactTitleLabelIconView.label =
                    if (numbers.size <= 0) "" else "${numbers[0].number} - ${numbers[0].type}"
            }

            contactTitleLabelIconView.setOnClickListener {
                recyclerViewClickListener.recyclerViewItemClicked(it, adapterPosition)
            }
        }
    }

    inner class EmptyViewHolder(itemView: View) : BaseViewHolder(itemView)

    companion object {

        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_EMPTY = 0
    }
}