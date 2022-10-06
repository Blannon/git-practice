package com.example.gate

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.gate.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityLoginBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog:ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth:FirebaseAuth
    private var email=""
    private var password=""
    private var username=""

    override fun onCreate(savedInstanceState: Bundle?) {

        binding=ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //configure actionbar
        actionBar= supportActionBar!!
        actionBar.title="Login"

        //configure progress dialog
        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init FirebaseAuth
        firebaseAuth=FirebaseAuth.getInstance()
        checkUser()

        //handle click, open SignUp activity
        binding.lackAccountTv.setOnClickListener{
            startActivity(Intent(this,SignupActivity::class.java))
        }
        //handle click begin login
        binding.Loginbtn.setOnClickListener{
            //before logging in, validate data
            validateData()
        }
    }

    private fun validateData() {
        email=binding.EmailEt.text.toString().trim()
        password=binding.PasswordEt.toString().trim()
        username=binding.UsernameEt.toString().trim()

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.EmailEt.error = "Invalid Email format"
        }
        else if (TextUtils.isEmpty(username)){
            //no username entered
            binding.UsernameEt.error="please enter username"
        }
        else if (TextUtils.isEmpty(password)){
            // no password entered
            binding.PasswordEt.error="please enter Password"
        }
        else{
            //data is validated, begin login
            firebaselogin()
        }

    }

    private fun firebaselogin() {
        //show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                //get user info
                val firebaseUser=firebaseAuth.currentUser
                val email=firebaseUser!!.email
                Toast.makeText(this,"logged In as $email", Toast.LENGTH_SHORT).show()

                //open Profile
                startActivity(Intent(this,ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener{
                // login failed
                progressDialog.dismiss()
                Toast.makeText(this,"Login failed due to $(e.message)",Toast.LENGTH_SHORT).show()
            }
    }



    private fun checkUser() {
        // If user is logged in go to profile Activity
        //get current user
        val  firebaseUser=firebaseAuth.currentUser
        if (firebaseUser !=null){
            //user is already logged In
            startActivity(Intent(this,ProfileActivity::class.java))
            finish()
        }
    }
}