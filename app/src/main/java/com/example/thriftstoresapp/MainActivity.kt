package com.example.thriftstoresapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    lateinit var emailEditText : EditText
    lateinit var passwordEditText : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        auth = Firebase.auth

        checkIfLoggedIn()

        val logInButton = findViewById<Button>(R.id.loginButton)
        logInButton.setOnClickListener {
            login()
        }
        val createUserButton = findViewById<Button>(R.id.createUserButton)
        createUserButton.setOnClickListener {
            createUser()
        }

    }

    private fun createUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter e-mail and password to set up user",
                Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show()
                    Log.d("!!!!", "create user successful")
                    goToPlaces()
                } else {
                    Toast.makeText(this, "user not created ${task.exception}",
                        Toast.LENGTH_SHORT).show()
                    Log.d("!!!!", "user not created ${task.exception}")
                }
            }
    }

    private fun login() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter e-mail and password to log in",
                Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show()
                    Log.d("!!!!", "User logged in")
                    goToPlaces()
                } else {
                    Toast.makeText(this, "User not logged in: ${task.exception}", Toast.LENGTH_SHORT).show()
                    Log.d("!!!!", "User not logged in: ${task.exception}")
                }
            }
    }

    private fun goToPlaces() {
        val intent = Intent(this, PlacesRecyclerView::class.java)
        startActivity(intent)

    }

    private fun checkIfLoggedIn() {
        if(auth.currentUser != null) {
            goToPlaces()
        }
    }
}