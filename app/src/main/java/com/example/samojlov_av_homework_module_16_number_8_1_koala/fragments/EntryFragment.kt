package com.example.samojlov_av_homework_module_16_number_8_1_koala.fragments

import android.R.attr.width
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.samojlov_av_homework_module_16_number_8_1_koala.MainActivity
import com.example.samojlov_av_homework_module_16_number_8_1_koala.R
import com.example.samojlov_av_homework_module_16_number_8_1_koala.databinding.FragmentEntryBinding
import com.google.firebase.auth.FirebaseAuth


class EntryFragment : Fragment() {

    private lateinit var binding: FragmentEntryBinding
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var confirmPasswordET: EditText
    private lateinit var entryBT: Button
    private lateinit var textViewInputFormTV: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEntryBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        emailET = binding.includeFormEntryForm.emailRegistrationET
        passwordET = binding.includeFormEntryForm.passwordRegistrationET
        confirmPasswordET = binding.includeFormEntryForm.confirmPasswordRegistrationET
        entryBT = binding.includeFormEntryForm.inputButtonBT
        textViewInputFormTV = binding.includeFormEntryForm.textViewInputFormTV

        changingTheInputForm()



        entryBT.setOnClickListener {
            login()

        }

        textViewInputFormTV.setOnClickListener {
            backTransition()
        }

    }

    private fun changingTheInputForm() {
        entryBT.text = getString(R.string.entryFragment_button_text)
        textViewInputFormTV.text = getString(R.string.entryFragment_textView_text)
        textViewInputFormTV.visibility = View.INVISIBLE
        confirmPasswordET.visibility = View.INVISIBLE
        confirmPasswordET.layoutParams = LayoutParams(width, 0)
        textViewInputFormTV.layoutParams = LayoutParams(width, 0)
    }

    private fun login() {
        val email = emailET.text.toString().trim()
        val pass = passwordET.text.toString().trim()

        val emptyFields = email.isBlank() || pass.isBlank()

        if (emptyFields) {
            Toast.makeText(
                context,
                getString(R.string.signUpUser_checkOne_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                Toast.makeText(
                    context,
                    getString(R.string.auth_login_true_toast), Toast.LENGTH_LONG
                ).show()
                transition()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.auth_login_false_toast), Toast.LENGTH_LONG
                ).show()
                textViewInputFormTV.visibility = View.VISIBLE
                textViewInputFormTV.layoutParams =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }
        }
    }

    private fun backTransition() {
        (activity as MainActivity).menuBack = false
        (activity as MainActivity).invalidateOptionsMenu()
        view?.findNavController()?.navigate(R.id.action_entryFragment_to_signUpFragment)
    }

    private fun transition() {
        view?.findNavController()?.navigate(R.id.action_entryFragment_to_contactsFragment)
    }

}