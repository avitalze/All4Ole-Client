package com.example.all4ole_client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistrationB : AppCompatActivity() {


    private lateinit var continueButton: Button
    private lateinit var etRegUsername: EditText
    private lateinit var etRegPassword: EditText
    private lateinit var etRegEmail: EditText
    private lateinit var maritalStatus: Spinner
    private lateinit var area: Spinner
    private lateinit var currUser: User
    private lateinit var url: String
    private lateinit var hasChildren: CheckBox
    private lateinit var helpCheckBoxes: ArrayList<CheckBox>
    private lateinit var hobbyCheckBoxes: ArrayList<CheckBox>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_b)
        currUser = intent.getParcelableExtra("currUser")!!
        url = intent.getStringExtra("theUrl")!!
        area = findViewById(R.id.areaSpinner)
        hasChildren = findViewById(R.id.hasChildren)
        setCheckBoxes()
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.residential_areas,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            area.adapter = adapter
        }
        maritalStatus = findViewById(R.id.statusSpinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.marital_status,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            maritalStatus.adapter = adapter
        }
    }

    private fun setCheckBoxes() {
        helpCheckBoxes = ArrayList<CheckBox>()
        hobbyCheckBoxes = ArrayList<CheckBox>()

        var mask: Int = 1
        for (x in 1..8) {
            val buttonHelpID = "btnHelp$x"
            val buttonHobbyID = "btnHobby$x"

            val resHelpID = resources.getIdentifier(buttonHelpID, "id", packageName)
            val resHobbyID = resources.getIdentifier(buttonHobbyID, "id", packageName)
            val helpBox: CheckBox = findViewById<View>(resHelpID) as CheckBox
            val hobbyBox: CheckBox = findViewById<View>(resHobbyID) as CheckBox
            hobbyCheckBoxes.add(hobbyBox)
            helpCheckBoxes.add(helpBox)
            mask = mask shl 1
        }
    }


    fun btnFinishOnClick(view: View) {
        val help: Int = getHelp()
        val hobbies: Int = getHobby()
        currUser.residentialArea = area.selectedItem.toString()
        currUser.help = help
        currUser.hobbies = hobbies
        currUser.hasLittleChildren = if (hasChildren.isChecked) {
            1
        } else {
            0
        }
        currUser.maritalStatus = if (maritalStatus.selectedItem.toString() == "Taken") {
            1
        } else {
            0
        }


        val gson = GsonBuilder().setLenient().create()
        val userJson: String = gson.toJson(currUser, User::class.java)
        val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), userJson)
        //Create the retrofit instance to issue with the network requests:
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

            //Defining the api for sending by the request
            val api = retrofit.create(Api::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                //Sending the request
                api.register(rb).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.message() == "OK" || response.isSuccessful) { // if ok we get all user details -how to get
                            // save details of curr user
                            println("Successfully posted - user registered")
                            val userFromServer: User = gson.fromJson(response.body()?.string(), User::class.java)
                            if (userFromServer == currUser) {
                                currUser = userFromServer
                                MainActivity.toastMessage(applicationContext, "Registered Successfully")
                                finish()
                            }
                        } else {
                            MainActivity.toastMessage(applicationContext, "userName or password is incorrect")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println(t.message.toString())
                        MainActivity.toastMessage(applicationContext, "Error in connection with server, go back to menu")
                    }
                })
            }
        } catch (e: Exception) {
            MainActivity.toastMessage(applicationContext, "Invalid url - Go back to menu")
        }
    }

    private fun getHelp(): Int {
        var help = 0
        for ((i, checkBox) in helpCheckBoxes.withIndex()) {
            if (checkBox.isChecked) {
                help = help or (1 shl i)
            }
        }
        return help
    }

    private fun getHobby(): Int {
        var hobbies = 0
        for ((i, checkBox) in hobbyCheckBoxes.withIndex()) {
            if (checkBox.isChecked) {
                hobbies = hobbies or (1 shl i)
            }
        }
        return hobbies
    }
}