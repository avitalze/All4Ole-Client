package com.example.all4ole_client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_registration_b.*
import kotlinx.android.synthetic.main.activity_registration_screen.*

class RegistrationScreen : AppCompatActivity() {

    private lateinit var editUserName: EditText
    private lateinit var editPassword: EditText
    private lateinit var editFirstName: EditText
    private lateinit var editLastName: EditText
    private lateinit var editPhone: EditText
    private lateinit var editEmail: EditText
    private lateinit var country: Spinner
    private lateinit var language: Spinner
    private lateinit var url: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_screen)
        url = intent.getStringExtra("theUrl")!!
        editUserName = findViewById(R.id.editUserName)
        editPassword = findViewById(R.id.editPassword)
        editEmail = findViewById(R.id.editEmail)
        editFirstName = findViewById(R.id.editFirstName)
        editLastName = findViewById(R.id.editLastName)
        editPhone = findViewById(R.id.editPhone)
        country = findViewById(R.id.countrySpinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.countries,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            country.adapter = adapter
        }

        language = findViewById(R.id.languageSpinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            language.adapter = adapter
        }
    }

    fun btnContinueOnClick(view: View) {
        // todo check if user exists

        val user = User(
            editPhone.text.toString(), editEmail.text.toString(), editFirstName.text.toString(),
            0, 0, 0, language.selectedItem.toString(), editLastName.text.toString(),
            0, country.selectedItem.toString(), editPassword.text.toString(), "", editUserName.text.toString()
        )


        val intent = Intent(this@RegistrationScreen, RegistrationB::class.java)
        intent.putExtra("currUser",user)
        intent.putExtra("theUrl",url)
        startActivity(intent)
        finish()

        // todo ooooooooooooooooooooo
        /*   userToeRegister.userName = etRegUsername.text.toString()
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
           }*/


    }


}