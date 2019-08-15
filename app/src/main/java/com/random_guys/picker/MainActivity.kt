package com.random_guys.picker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.random_guys.pica.*
import com.random_guys.rv.LoadMoreListener
import com.random_guys.rv.RecyclerScrollMoreListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ContactSelectedListener, LoadMoreListener {

    private var mContacts = ArrayList<Contact>()
    private lateinit var contactAdapter: SelectContactAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerScrollMoreListener: RecyclerScrollMoreListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init contact picker
        val pica = Pica(this)

        contactAdapter = SelectContactAdapter(this)
        contactAdapter.mContactSelectedListener = this
        contactAdapter.loadMoreListener = this

        // load contacts into adapter
        pica.chu { mContacts = it }
        contactAdapter.add(mContacts.subList(0, 20))

        linearLayoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = contactAdapter

        recyclerScrollMoreListener = RecyclerScrollMoreListener(linearLayoutManager, contactAdapter)
        recyclerView.addOnScrollListener(recyclerScrollMoreListener)
    }

    override fun onLoadMore(page: Int, total: Int) {
        if (mContacts.size >= total) {
            contactAdapter.add(mContacts.subList(page * 10, (page * 10) + 10))
            recyclerView.post { contactAdapter.notifyItemInserted(contactAdapter.itemCount) }
        }
    }

    override fun onContactSelected(v: View, position: Int) {
        Toast.makeText(this, contactAdapter.get(position).toString(), LENGTH_SHORT).show()
    }
}
