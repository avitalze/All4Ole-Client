package com.example.all4ole_client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registration_screen.*

class RegistrationScreen : AppCompatActivity() {

    private lateinit var registerButton: Button
    private lateinit var etRegUsername: EditText
    private lateinit var etRegPassword: EditText
    private lateinit var etRegEmail: EditText


    private lateinit var userToeRegister: User



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_screen)

        userToeRegister= User()


        registerButton = findViewById(R.id.btnRegister) // registerBtn
        etRegUsername = findViewById(R.id.editUserName)
        etRegPassword = findViewById(R.id.editPassword)
        etRegEmail=findViewById(R.id.editEmail)
        //   move to login screen
        fun loginOnClick(view: View){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
//editEmail

    fun btnRegisterOnClick(view: View){

        userToeRegister.userName = etRegUsername.text.toString()
        userToeRegister.password = etRegPassword.text.toString()
        userToeRegister.email = etRegEmail.text.toString()

        // do func in person that check all details full !
        if(editUserName.text.isNotEmpty() && editEmail.text.isNotEmpty() && editPassword.text.isNotEmpty()){
            // do
            Toast.makeText(this,"input is ok", Toast.LENGTH_LONG).show()
            println("-------------------curr user name is: "+userToeRegister.userName+"-------------------" )
            println("-------------------curr password is: "+userToeRegister.password+"---------------------" )
            println("-------------------curr password is: "+userToeRegister.email+"---------------------" )

            // todo create user from all the fields

            // todo takes the user and make it a json to send to the server
                //val outputJson: String = Gson().toJson(myClass)

            // todo send to the server and go to home page


        }
        // empty userName or password
        else{
            Toast.makeText(this,"input not full", Toast.LENGTH_LONG).show()
        }


    }




}