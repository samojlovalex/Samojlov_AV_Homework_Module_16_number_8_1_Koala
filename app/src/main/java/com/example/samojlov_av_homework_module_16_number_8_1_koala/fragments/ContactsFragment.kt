package com.example.samojlov_av_homework_module_16_number_8_1_koala.fragments

import android.R.attr.width
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.samojlov_av_homework_module_16_number_8_1_koala.R
import com.example.samojlov_av_homework_module_16_number_8_1_koala.databinding.FragmentContactsBinding
import com.example.samojlov_av_homework_module_16_number_8_1_koala.models.Contact
import com.example.samojlov_av_homework_module_16_number_8_1_koala.utils.MyContactAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

const val DATA_BASE = "users"

@Suppress("UNCHECKED_CAST")
class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var nameContactTV: EditText
    private lateinit var phoneContactTV: EditText
    private lateinit var confirmPasswordET: EditText
    private lateinit var saveBT: Button
    private lateinit var textViewInputFormTV: TextView
    private lateinit var listOfContactsRV: RecyclerView

    private var adapter: MyContactAdapter? = null
    private var listOfContacts = mutableListOf<Contact>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        nameContactTV = binding.includeFormContactsForm.emailRegistrationET
        phoneContactTV = binding.includeFormContactsForm.passwordRegistrationET
        confirmPasswordET = binding.includeFormContactsForm.confirmPasswordRegistrationET
        saveBT = binding.includeFormContactsForm.inputButtonBT
        textViewInputFormTV = binding.includeFormContactsForm.textViewInputFormTV
        listOfContactsRV = binding.listOfContactsRV

        listOfContactsRV.layoutManager = LinearLayoutManager(context)

        changingTheInputForm()

        initAdapter()

        saveBT.setOnClickListener {
            createANewRow()
        }

        updateDB()
    }

    private fun updateDB() {
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        val database = Firebase.database.reference.child(DATA_BASE).child(id)
        database.addValueEventListener(contactListener)
    }

    private fun createANewRow() {
        val name = nameContactTV.text.toString()
        val phone = phoneContactTV.text.toString()

        val emptyFields = name.isBlank() || phone.isBlank()

        if (emptyFields) {
            Toast.makeText(
                context,
                getString(R.string.signUpUser_checkOne_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val contactAdd = Contact(name, phone)
        addContact(contactAdd)
        nameContactTV.text.clear()
        phoneContactTV.text.clear()
    }

    private fun addContact(contact: Contact) {
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        val database = Firebase.database.reference
            .child(DATA_BASE)
            .child(id)
        val map: HashMap<String, Contact> = HashMap()
        val key: String = contact.toString()
        map[key] = contact
        database.updateChildren(map as Map<String, Any>)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        adapter = MyContactAdapter(listOfContacts.toList())
        listOfContactsRV.adapter = adapter
        adapter!!.setContactClickListener(object : MyContactAdapter.OnContactClickListener {
            override fun onContactClickListener(contact: Contact, position: Int) {
                deleteRow(contact)
            }
        })
        adapter?.notifyDataSetChanged()
    }

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    private fun deleteRow(contact: Contact) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val alert = dialogBuilder.create()
        val dialogValues = inflater.inflate(R.layout.delete_row_alert_dialog, null)
        alert.setView(dialogValues)
        dialogValues.setBackgroundColor(Color.parseColor("#FFFFFF"))
        alert.show()

        val message = dialogValues.findViewById<TextView>(R.id.delete_messageTV)
        val cancel =
            dialogValues.findViewById<Button>(R.id.delete_alter_dialog_button_cancelBT)
        val delete =
            dialogValues.findViewById<Button>(R.id.delete_enter_alter_dialog_button_deleteBT)

        message.text = getString(R.string.delete_alter_dialog_message_text)

        cancel.setOnClickListener {
            alert.cancel()
        }

        delete.setOnClickListener {
            val id = FirebaseAuth.getInstance().currentUser!!.uid
            val database = Firebase.database.reference
                .child(DATA_BASE)
                .child(id)
            val map: HashMap<String, Contact?> = HashMap()
            val key = contact.toString()
            map[key] = null
            database.updateChildren(map as Map<String, Any>)

            Toast.makeText(
                context,
                getString(R.string.delete_alter_dialog_toast, contact.name), Toast.LENGTH_LONG
            ).show()
            alert.cancel()
            adapter?.notifyDataSetChanged()
        }
    }

    private fun changingTheInputForm() {
        nameContactTV.inputType = InputType.TYPE_CLASS_TEXT
        nameContactTV.setAutofillHints(null)
        nameContactTV.setHint(getString(R.string.nameContactTV_hint))

        phoneContactTV.inputType = InputType.TYPE_CLASS_PHONE
        phoneContactTV.setAutofillHints(null)
        phoneContactTV.setHint(getString(R.string.phoneContactTV_hint))

        confirmPasswordET.visibility = View.INVISIBLE
        confirmPasswordET.layoutParams = LayoutParams(width, 0)

        textViewInputFormTV.visibility = View.INVISIBLE
        textViewInputFormTV.layoutParams = LayoutParams(width, 0)

        saveBT.text = getString(R.string.saveBT_text)
    }

    private val contactListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            listOfContacts.clear()
            val map = snapshot.getValue<Map<String, Contact>>()
            if (map != null) {
                for (contact in map.values) listOfContacts.add(contact)
            }
            initAdapter()
        }

        override fun onCancelled(error: DatabaseError) {
            val id = FirebaseAuth.getInstance().currentUser!!.uid
            Toast.makeText(context, "$id не обновлена", Toast.LENGTH_LONG).show()
        }
    }
}