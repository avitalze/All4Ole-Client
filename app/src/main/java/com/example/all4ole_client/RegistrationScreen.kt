package com.example.all4ole_client

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

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

    companion object {
        const val MY_REQUEST_CODE = 1
    }


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

    // go to other registration screen if all fields are set
    fun btnContinueOnClick(view: View) {

        val userName: Pair<String, String> = Pair(editUserName.text.toString(), "userName")
        val password: Pair<String, String> = Pair(editPassword.text.toString(), "password")
        val email: Pair<String, String> = Pair(editEmail.text.toString(), "email")
        val firstName: Pair<String, String> = Pair(editFirstName.text.toString(), "firstName")
        val lastName: Pair<String, String> = Pair(editLastName.text.toString(), "lastName")
        val phone: Pair<String, String> = Pair(editPhone.text.toString(), "phone")

        val allEditText: List<Pair<String, String>> = listOf(userName, password, email, firstName, lastName, phone)

        for (parameter in allEditText) {
            if (parameter.first == "") {
                LoginScreen.toastMessage(this, "You didn't write ${parameter.second}")
                return
            }
        }

        val languageStr: String = language.selectedItem.toString()
        val countryStr: String = country.selectedItem.toString()

        if(countryStr== resources.getStringArray(R.array.countries)[0]){
            LoginScreen.toastMessage(this, "You didn't choose previous country!")
            return
        }

        if(languageStr== resources.getStringArray(R.array.languages)[0]){
            LoginScreen.toastMessage(this, "You didn't choose language!")
            return
        }

        val user = User(
            editPhone.text.toString(), editEmail.text.toString(), editFirstName.text.toString(),
            0, 0, 0, language.selectedItem.toString(), editLastName.text.toString(),
            0, country.selectedItem.toString(), editPassword.text.toString(), "", editUserName.text.toString()
        )


        val intent = Intent(this@RegistrationScreen, RegistrationB::class.java)
        intent.putExtra("currUser", user)
        intent.putExtra("theUrl", url)
        startActivityForResult(intent, MY_REQUEST_CODE)
    }

    // if the user ended the registration in the second screen it goes back to login screen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }
}