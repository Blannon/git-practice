package com.example.gate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.gate.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    //viewBinding
    private lateinit var binding: ActivityProfileBinding
    //ActionBar
    private lateinit var actionBar: ActionBar
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // configure action bar
        actionBar=supportActionBar!!
        actionBar.title="Profile"

        //init firebase auth
        firebaseAuth=FirebaseAuth.getInstance()
        checkuser()

        //handle click logout
        binding.logoutbtn.setOnClickListener{
            firebaseAuth.signOut()
            checkuser()
        }

    }

    private fun checkuser() {
        //check user is logged in or not
        val firebaseUser=firebaseAuth.currentUser
        if (firebaseUser !=null){
            // user not null, user is logged in, get user info
            val email=firebaseUser.email
            // set to text view
            binding.emailTv.text=email

        }
        else{
            //user is null, user is not logged in, goto login activity
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}