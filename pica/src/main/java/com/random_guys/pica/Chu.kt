package com.random_guys.pica

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.random_guys.rv.LoadMoreListener
import com.random_guys.rv.RecyclerScrollMoreListener
import java.util.*

class Chu(
    private val mMainContacts: ArrayList<Contact>,
    private val mContactClickListener: ContactClickListener
) :
    BottomSheetDialogFragment(),
    ContactSelectedListener,
    LoadMoreListener {


    // region Views
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSearchEditText: EditText
    private lateinit var mContactAdapter: SelectContactAdapter
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mRecyclerScrollMoreListener: RecyclerScrollMoreListener
    // endregion

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_select_contact_bottom_sheet, container, false)

        // init contact picker
        activity?.let {
            bindViews(view)

            mContactAdapter = SelectContactAdapter(it.baseContext, this)
            mContactAdapter.loadMoreListener = this

            mLinearLayoutManager = LinearLayoutManager(it.baseContext)
            mRecyclerScrollMoreListener =
                RecyclerScrollMoreListener(mLinearLayoutManager, mContactAdapter)

            mRecyclerView.layoutManager = mLinearLayoutManager
            mRecyclerView.adapter = mContactAdapter
            mRecyclerView.addOnScrollListener(mRecyclerScrollMoreListener)

            loadContacts()
            initSearchView()
        }

        return view
    }

    private fun loadContacts() {
        when (mMainContacts.size) {
            in 0..9 -> mContactAdapter.addMany(mMainContacts)
            else -> mContactAdapter.addMany(mMainContacts.subList(0, 10))
        }
    }

    private fun bindViews(view: View) {
        mSearchEditText = view.bind(R.id.search)
        mRecyclerView = view.bind(R.id.recyclerView)
    }

    private fun initSearchView() {
        mSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(query: Editable?) {
                query?.toString()?.trim()?.let { _query ->
                    mContactAdapter.clear()
                    val mSearchableContacts = HashMap<String, Contact>()

                    val tempContacts = mMainContacts.filter { contact ->
                        contact.name.contains(_query, true)
                    }

                    if (tempContacts.isEmpty()) {
                        when {
                            _query.isEmpty() -> loadContacts()
                            else -> mContactAdapter.clear()
                        }
                    } else {
                        tempContacts.forEach { mSearchableContacts[it.id] = it }
                        mContactAdapter.addMany(mSearchableContacts.values)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    override fun onLoadMore(page: Int, total: Int) {
        if (mMainContacts.size >= total) {
            try {
                mContactAdapter.addMany(
                    mMainContacts.subList(
                        page * 10,
                        (page * 10) + 10
                    )
                )
            } catch (e: IndexOutOfBoundsException) {

            }
        }
    }

    interface ContactClickListener {
        fun onContactClickListener(contact: Contact)
    }

//    private fun refreshAdapter() = mRecyclerView.post { mContactAdapter.notifyDataSetChanged() }

    override fun onContactSelected(v: View, position: Int) {
        mContactClickListener.onContactClickListener(mMainContacts[position])
        dismiss()
    }
}