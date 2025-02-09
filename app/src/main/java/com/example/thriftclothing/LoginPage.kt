package com.example.thriftclothing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.thriftclothing.databinding.ActivityLoginPageBinding
import com.example.thriftclothing.ui.activity.DashboardActivity
import com.google.firebase.auth.FirebaseAuth

class LoginPage : AppCompatActivity() {

    lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth  // Firebase Authentication instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.navigateRegistration.setOnClickListener {
            val intent = Intent(this, RegistrationPage::class.java)
            startActivity(intent)
            finish()
        }

        binding.button.setOnClickListener {
            val email = binding.EmailorNumber.text.toString()
            val password = binding.password.text.toString()

            // Validate email and password
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Attempt to sign in the user with Firebase Authentication
            signInUser(email, password)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun signInUser(email: String, password: String) {
        // Sign in with Firebase Auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to the home screen
                    Log.d("LoginPage", "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Authentication successful.", Toast.LENGTH_SHORT).show()

                    // Redirect to the next activity (e.g., home page)
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user
                    Log.w("LoginPage", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
