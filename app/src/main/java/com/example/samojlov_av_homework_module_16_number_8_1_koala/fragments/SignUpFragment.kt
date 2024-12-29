package com.example.samojlov_av_homework_module_16_number_8_1_koala.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.samojlov_av_homework_module_16_number_8_1_koala.MainActivity
import com.example.samojlov_av_homework_module_16_number_8_1_koala.R
import com.example.samojlov_av_homework_module_16_number_8_1_koala.databinding.FragmentSignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var emailET: EditText
    private lateinit var passwordET: EditText
    private lateinit var confirmPasswordET: EditText
    private lateinit var signUpBT: Button
    private lateinit var textViewInputFormTV: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        emailET = binding.includeFormRegistrationForm.emailRegistrationET
        passwordET = binding.includeFormRegistrationForm.passwordRegistrationET
        confirmPasswordET = binding.includeFormRegistrationForm.confirmPasswordRegistrationET
        signUpBT = binding.includeFormRegistrationForm.inputButtonBT
        textViewInputFormTV = binding.includeFormRegistrationForm.textViewInputFormTV

        signUpBT.text = getString(R.string.signUpFragment_button_text)
        signUpBT.setOnClickListener {
            signUpUser()
        }

        textViewInputFormTV.setOnClickListener {
            transition()
        }
    }

    private fun transition() {
        (activity as MainActivity).menuBack = true
        (activity as MainActivity).invalidateOptionsMenu()
        view?.findNavController()?.navigate(R.id.action_signUpFragment_to_entryFragment)
    }

    private fun signUpUser() {
        val email = emailET.text.toString().trim()
        val pass = passwordET.text.toString().trim()
        val confirmPass = confirmPasswordET.text.toString().trim()

        val emptyFields = email.isBlank() || pass.isBlank() || confirmPass.isBlank()
        val confirmPasswordCheck = pass != confirmPass

        if (emptyFields) {
            Toast.makeText(
                context,
                getString(R.string.signUpUser_checkOne_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (confirmPasswordCheck) {
            Toast.makeText(
                context,
                getString(R.string.signUpUser_checkTwo_toast),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                Toast.makeText(
                    context,
                    getString(R.string.auth_signUp_true_toast), Toast.LENGTH_LONG
                ).show()
                transition()
            } else{
                if (auth.currentUser != null){
                    Toast.makeText(
                        context,
                        getString(R.string.auth_signUp_else_true_toast), Toast.LENGTH_LONG
                    ).show()
                    transition()
                }else{
                    Toast.makeText(
                        context,
                        getString(R.string.auth_signUp_else_false_toast), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        backCheck()
    }

    private fun backCheck() {
        val fragmentInstance =
            requireActivity().supportFragmentManager.findFragmentById(R.id.signUpFragment)
        (activity as MainActivity).menuBack = fragmentInstance is SignUpFragment
        (activity as MainActivity).invalidateOptionsMenu()
    }

}