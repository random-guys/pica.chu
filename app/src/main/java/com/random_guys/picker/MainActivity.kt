package com.random_guys.picker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.random_guys.pica.Chu
import com.random_guys.pica.Contact
import com.random_guys.pica.Pica
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Chu.DismissListener, Chu.ContactClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val mMainContacts = ArrayList<Contact>()

            // load contacts
            val pica = Pica(this)
            pica.load { contacts -> mMainContacts.addAll(contacts) }

            // open contacts picker
            val chooser = Chu(mMainContacts, this, this)
            chooser.show(supportFragmentManager, "")
        }
    }

    override fun onDismissed() {

    }

    override fun onContactClickListener(contact: Contact) {
        Toast.makeText(this, contact.name, Toast.LENGTH_LONG).show()
    }

    private fun hideKeyboard() {
        val view: View? = currentFocus
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
