package com.example.chatapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.BaseActivity
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivitySignUpBinding
import com.example.chatapp.model.ContactDetails
import com.example.chatapp.model.User
import com.example.chatapp.utils.CommanFunction
import com.example.chatapp.utils.CommonMethods
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this);
        fireBaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")
        onClickEvents()

    }

    private fun onClickEvents() {
        binding.tvSubmit.setOnClickListener {

            if (isValidaton()) {
                signUpWithEmailPassword(
                    binding.emailEt.text.toString(),
                    binding.passwordEt.text.toString()
                )

            }

            if(binding.lnUserName.visibility==View.VISIBLE)
            {
                if (binding.usernameEt.text.toString().isEmpty() ) {
                    CommanFunction.showToast(this,"Please Enter Your Name")
                }else{
                    Log.d("chaljana222", "addProfile: ")
                    addProfile(binding.usernameEt.text.toString())
                }

            }

        }


    }

    private fun addProfile(userName: String) {
        Log.d("chaljana111", "addProfile: ")
        val userId = fireBaseAuth.currentUser?.uid
        if (userId != null) {
            val user = User(userId, userName, "")
            database.child(userId).setValue(user)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("chaljana333", "addProfile: ")
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Log.d("chaljana444 ", "addProfile: ")
                        Toast.makeText(
                            this, "Failed to save user data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun signUpWithEmailPassword(email: String, password: String) {
        fireBaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.lnSignUp.visibility = View.GONE
                    binding.lnUserName.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        baseContext, "Sign up failed. ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun isValidaton(): Boolean {
        if (binding.emailEt.text.toString().isEmpty()
        ) {
            CommanFunction.showToast(this, "Please Enter Your Email")
            return false
        } else if (!CommonMethods.emailValidation(binding.emailEt.text.toString())
        ) {
            CommanFunction.showToast(this, "Please Enter Valid email")
            return false
        } else if (binding.passwordEt.text.toString().isEmpty()) {
            CommanFunction.showToast(this, "Please Enter Password")
            return false
        } else {
            return true
        }
    }
}