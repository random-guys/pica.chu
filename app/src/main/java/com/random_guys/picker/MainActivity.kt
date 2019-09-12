package com.random_guys.picker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.random_guys.pica.Chu
import com.random_guys.pica.MainContact
import com.random_guys.pica.Pica
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Chu.ContactClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val mMainContacts = ArrayList<MainContact>()

            // load contacts
            val pica = Pica(this)
            pica.load { contacts -> mMainContacts.addAll(contacts) }

            // open contacts picker
            val chooser = Chu(mMainContacts, this)
            chooser.show(supportFragmentManager, "")
        }
    }

    override fun onContactClickListener(contact: MainContact) {
        Toast.makeText(this, contact.name, Toast.LENGTH_LONG).show()
    }
}
