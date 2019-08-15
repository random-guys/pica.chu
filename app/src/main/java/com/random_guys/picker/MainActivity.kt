package com.random_guys.picker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.random_guys.pica.Pica
import com.random_guys.pica.ContactSelectedListener
import com.random_guys.pica.SelectContactAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ContactSelectedListener {

    private lateinit var contactAdapter: SelectContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactAdapter = SelectContactAdapter(this)
        contactAdapter.mContactSelectedListener = this
        val pica = Pica(this)
        pica.chu { contactAdapter.add(it) }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter
    }

    override fun onContactSelected(v: View, position: Int) {
        Toast.makeText(this, contactAdapter.get(position).toString(), LENGTH_SHORT).show()
    }
}
