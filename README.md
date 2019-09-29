# pica.chu
This is tiny contacts picker as a BottomSheetDialog picker for Android. 
We built it because we wanted to combine phone contacts on a device with contacts loaded from a server.
Also some contact pickers were just misbehaving and we needed the experience to be consistent across devices.

This lib will be useful to anyone that's frustrated using the default contact pickers that ship with android devices.
This is due to the fact that each device rolls out their contact picker.

## Demo

![Picachu](https://media.giphy.com/media/WodomZmSBBMQQYSnxs/giphy.gif)

## Usage

Add these permissions to `AndroidManifest.xml`
```xml
    <uses-permission android:name="android.permission.READ_CONTACTS"/>
    <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
```

Usage in View (`Activity` or `Fragment`)
```kt
import com.random_guys.pica.Chu
import com.random_guys.pica.Contact
import com.random_guys.pica.Pica

class MainActivity : AppCompatActivity(), Chu.ContactClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val mMainContacts = ArrayList<Contact>()

            // load contacts from phonebook into an array
            val pica = Pica(this)
            pica.load { contacts -> mMainContacts.addAll(contacts) }

            // open contacts picker using contacts loaded from phonebook
            val chooser = Chu(mMainContacts, this)
            chooser.show(supportFragmentManager, "")
        }
    }

    override fun onContactClickListener(contact: Contact) {
        Toast.makeText(this, contact.name, Toast.LENGTH_LONG).show()
    }
}
```
