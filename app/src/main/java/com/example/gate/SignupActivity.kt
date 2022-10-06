package com.example.gate

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.gate.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding:ActivitySignupBinding

    //ActionBar
    private lateinit var actionBar: ActionBar
    //ProgressiveDialog
    private lateinit var progressDialog:ProgressDialog
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email=""
    private var password=""
    private var username=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure Actionbar, // enable back button
        actionBar=supportActionBar!!
        actionBar.title="Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowCustomEnabled(true)

        //configure progress dialog
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Signing Up...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth=FirebaseAuth.getInstance()

        //handle click, begin signup
        binding.SignUpbtn.setOnClickListener{
            //validate data
            validateData()
        }
    }

    private fun validateData() {
        //get data
        email=binding.EmailEt.text.toString().trim()
        password=binding.PasswordEt.text.toString().trim()

        //validate data
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
        else if (password.length <6) {
            //password length less than si characters
            binding.PasswordEt.error = "Password must be at least be 6 characters long"
        }
        //data is validated, begin login
            firebaseSignup()
        }

    private fun firebaseSignup() {
        //show progress
        progressDialog.show()

        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnFailureListener{
                //signup success
                progressDialog.dismiss()
                //get current user
                val firebaseUser=firebaseAuth.currentUser
                val email=firebaseUser!!.email
                Toast.makeText(this,"Account created with email $email",Toast.LENGTH_SHORT).show()
                //open profile
                startActivity((Intent(this,ProfileActivity::class.java)))
                finish()
    }
            .addOnFailureListener{
                //signup failed
                progressDialog.dismiss()
                Toast.makeText(this,"SignUp Failed due to $(e.message)",Toast.LENGTH_SHORT).show()
            }

}

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()//Go back to previous activity, when back button of Action bar clicked
        return super.onSupportNavigateUp()
    }
}