package com.random_guys.pica

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
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import java.util.*

class Chu(
    private val mMainContacts: ArrayList<MainContact>,
    private val mContactClickListener: ContactClickListener
) :
    BottomSheetDialogFragment(),
    ContactSelectedListener {

    // region Views
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mSearchEditText: EditText
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    // endregion

    private lateinit var mFastAdapter: FastAdapter<MainContact>
    private lateinit var mMainContactsAdapter: ItemAdapter<MainContact>

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

            mMainContactsAdapter = ItemAdapter()
            mMainContactsAdapter.add(mMainContacts)
            mFastAdapter = FastAdapter.with(mMainContactsAdapter)

            mLinearLayoutManager = LinearLayoutManager(it.baseContext)

            mRecyclerView.layoutManager = mLinearLayoutManager
            mRecyclerView.adapter = mFastAdapter

            initSearchView()
        }

        return view
    }

    private fun bindViews(view: View) {
        mSearchEditText = view.bind(R.id.search)
        mRecyclerView = view.bind(R.id.recyclerView)
    }

    private fun initSearchView() {
        mSearchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(query: Editable?) {
                query?.toString()?.trim()?.let { _query ->
                    mMainContactsAdapter.filter(_query)
                    mMainContactsAdapter.itemFilter.filterPredicate =
                        { item: MainContact, constraint: CharSequence? ->
                            if (constraint.isNullOrEmpty()) mMainContactsAdapter.itemFilter.resetFilter()
                            item.name.contains(
                                constraint.toString(),
                                true
                            )
                        }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    interface ContactClickListener {
        fun onContactClickListener(contact: MainContact)
    }

    override fun onContactSelected(v: View, position: Int) {
        mContactClickListener.onContactClickListener(mMainContacts[position])
        dismiss()
    }
}