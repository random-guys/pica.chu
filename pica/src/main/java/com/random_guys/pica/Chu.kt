package com.random_guys.pica

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.FloatingSearchView.OnSearchListener
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.random_guys.rv.LoadMoreListener
import com.random_guys.rv.RecyclerScrollMoreListener
import java.util.*
import kotlin.collections.HashMap

class Chu(
    private val mMainContacts: ArrayList<Contact>,
    private val mDismissListener: DismissListener
) :
    BottomSheetDialogFragment(),
    ContactSelectedListener,
    LoadMoreListener {

    // contacts list based on the filter from the search view
    private var mSearchableContacts = HashMap<String, Contact>()

    // region Views
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mContactAdapter: SelectContactAdapter
    private lateinit var mFloatingSearchView: FloatingSearchView
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

            loadContacts()

            mLinearLayoutManager = LinearLayoutManager(it.baseContext)

            mRecyclerView.layoutManager = mLinearLayoutManager
            mRecyclerView.adapter = mContactAdapter

            mRecyclerScrollMoreListener =
                RecyclerScrollMoreListener(mLinearLayoutManager, mContactAdapter)
            mRecyclerView.addOnScrollListener(mRecyclerScrollMoreListener)

            initSearchView()
        }

        return view
    }

    private fun loadContacts() {
        when (mMainContacts.size) {
            in 0..9 -> mContactAdapter.addMany(mMainContacts) { refreshAdapter() }
            else -> mContactAdapter.addMany(mMainContacts.subList(0, 10)) { refreshAdapter() }
        }
    }

    private fun bindViews(view: View) {
        mRecyclerView = view.bind(R.id.recyclerView)
        mFloatingSearchView = view.bind(R.id.floating_search_view)
    }

    private fun initSearchView() {
        mFloatingSearchView.setShowSearchKey(true)
        mFloatingSearchView.setOnQueryChangeListener { _, newQuery ->
            if (newQuery.isEmpty()) {
                mSearchableContacts.clear()
                mFloatingSearchView.clearSuggestions()
                mContactAdapter.clear { refreshAdapter() }
                loadContacts()
            } else {
                mContactAdapter.clear { refreshAdapter() }
                mSearchableContacts.clear()

                val tempContacts = mMainContacts.filter { contact ->
                    contact.body.contains(
                        newQuery.toLowerCase(Locale.ENGLISH)
                    )
                }
                tempContacts.forEach { mSearchableContacts[it.id] = it }

                mContactAdapter.addMany(mSearchableContacts.values) { refreshAdapter() }
            }
        }

        mFloatingSearchView.setOnSearchListener(object : OnSearchListener {
            override fun onSearchAction(currentQuery: String) {
                mContactAdapter.clear { refreshAdapter() }
                mSearchableContacts.clear()

                val tempContacts =
                    mMainContacts.filter { contact ->
                        contact.body.contains(
                            currentQuery.toLowerCase(
                                Locale.ENGLISH
                            )
                        )
                    }
                tempContacts.forEach { mSearchableContacts[it.id] = it }

                mContactAdapter.addMany(mSearchableContacts.values) { refreshAdapter() }
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion) {
                val clone =
                    mSearchableContacts.filter {
                        it.value.body.contains(
                            searchSuggestion.body.toLowerCase(
                                Locale.ENGLISH
                            )
                        )
                    }

                mFloatingSearchView.swapSuggestions(clone.values.toList())
                mContactAdapter.clear { refreshAdapter() }

                loadContacts()
            }
        })

        mFloatingSearchView.setOnClearSearchActionListener {
            mSearchableContacts.clear()
            mFloatingSearchView.clearSuggestions()
            mContactAdapter.clear { refreshAdapter() }
            mContactAdapter.addMany(mMainContacts) { refreshAdapter() }
        }

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        mFloatingSearchView.setOnSuggestionsListHeightChanged { newHeight ->
            mRecyclerView.translationY = newHeight
        }
    }

    override fun onLoadMore(page: Int, total: Int) {
        if (mMainContacts.size >= total) {
            try {
                mContactAdapter.addMany(
                    mMainContacts.subList(
                        page * 10,
                        (page * 10) + 10
                    )
                ) { refreshAdapter() }
            } catch (e: IndexOutOfBoundsException) {

            }
        }
    }

    override fun dismiss() {
        mDismissListener.onDismissed()
        super.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        mDismissListener.onDismissed()
        super.onDismiss(dialog)
    }

    interface DismissListener {
        fun onDismissed()
    }

    private fun refreshAdapter() = mRecyclerView.post { mContactAdapter.notifyDataSetChanged() }

    override fun onContactSelected(v: View, position: Int) {
        mDismissListener.onDismissed()
        dismiss()
    }
}