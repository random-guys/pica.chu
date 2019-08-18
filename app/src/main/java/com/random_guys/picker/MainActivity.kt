package com.random_guys.picker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.random_guys.pica.Chu
import com.random_guys.pica.Contact
import com.random_guys.pica.Pica
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val mMainContacts = ArrayList<Contact>()

            // load contacts
            val pica = Pica(this)
            pica.load { contacts -> mMainContacts.addAll(contacts) }

            // open contacts picker
            val chooser = Chu(mMainContacts)
            chooser.show(supportFragmentManager, "")
        }
    }
}
