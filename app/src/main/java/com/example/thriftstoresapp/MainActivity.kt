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

        /*
        If a user is already logged in, we move directly to PlacesRecyclerView
         */
        checkIfLoggedIn()

        /*
        Lets set up the buttons!
         */

        val logInButton = findViewById<Button>(R.id.loginButton)
        logInButton.setOnClickListener {
            login()
        }
        val createUserButton = findViewById<Button>(R.id.createUserButton)
        createUserButton.setOnClickListener {
            createUser()
        }

    }

    /*
    If the create user button is clicked, we try to create a new user, and login.
     */

    private fun createUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        /*
        Check to see if the user has entered e-mail and desired password. Returns early if that is
        not the case (and displays a Toast)
        */

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter e-mail and password to set up user",
                Toast.LENGTH_SHORT).show()
            return
        }

        /*
        Tries to create a user. Shows a Toast on completion, which tells the user if the operation
        was successful or not.
         */
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

    /*
    Called when the login button is clicked. Uses the entered e-mail and password to log in the
    user.
     */
    private fun login() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        /*
        If the user hasn't entered email and/or password, we return early and get some Toast.
         */
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter e-mail and password to log in",
                Toast.LENGTH_SHORT).show()
            return
        }

        /*
        Tries to login. Have some Toast!
         */
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

    /*
    Starts PlacesRecyclerViewActivity.
     */
    private fun goToPlaces() {
        val intent = Intent(this, PlacesRecyclerViewActivity::class.java)
        startActivity(intent)

    }


    /*
    If a user is logged in, move directly to PlacesRecyclerViewActivity.
     */
    private fun checkIfLoggedIn() {
        if(auth.currentUser != null) {
            goToPlaces()
        }
    }
}