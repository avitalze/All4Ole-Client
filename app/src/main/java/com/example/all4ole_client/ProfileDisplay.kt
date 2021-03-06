package com.example.all4ole_client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileDisplay : AppCompatActivity() {
    private lateinit var currUser: User
    private var isMyUser: Boolean = true
    private lateinit var setHelpButton: Button
    private lateinit var helpCheckBoxes: ArrayList<CheckBox>
    private lateinit var hobbyCheckBoxes: ArrayList<CheckBox>
    private lateinit var url: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_display)
        currUser = intent.getParcelableExtra("currUser")!!
        isMyUser = intent.getBooleanExtra("isMyUser", true)
        url = intent.getStringExtra("theUrl")!!
        setHelpButton = findViewById(R.id.btnSetHelp)

        if (!isMyUser) {
            setHelpButton.visibility = View.INVISIBLE
        }

        makeTextViews()
        setCheckBoxes()


    }

    private fun makeTextViews() {
        var b: TextView = findViewById(R.id.txtPhone)
        b.text = applicationContext.getString(R.string.phone, currUser.cell)
        b = findViewById(R.id.txtEmail)
        b.text = applicationContext.getString(R.string.email, currUser.email)
        b = findViewById(R.id.txtCountry)
        b.text = applicationContext.getString(R.string.country, currUser.originCountry)
        b = findViewById(R.id.txtArea)
        b.text = applicationContext.getString(R.string.area, currUser.residentialArea)
        b = findViewById(R.id.txtLanguage)
        b.text = applicationContext.getString(R.string.language, currUser.language)
        b = findViewById(R.id.txtStatus)
        var status: String? = if (currUser.maritalStatus == 1) {
            "Taken"
        } else {
            "Not Taken"
        }

        b.text = applicationContext.getString(R.string.marital_status, status)
        status = if (currUser.hasLittleChildren == 1) {
            "Has"
        } else {
            "No"
        }
        b = findViewById(R.id.txtChildren)
        b.text = applicationContext.getString(R.string.children, status)
        if (!isMyUser) {
            b = findViewById(R.id.txtProfile)
            b.text = applicationContext.getString(R.string.otherProfileDisplay, currUser.firstName, currUser.lastName)
        }

        if (!isMyUser) {
            b = findViewById(R.id.txtProfile)
            b.text = applicationContext.getString(R.string.otherProfileDisplay, currUser.firstName, currUser.language)
        }
    }

    // set check boxes with V if the user has this hobby/help
    private fun setCheckBoxes() {
        helpCheckBoxes = ArrayList<CheckBox>()
        hobbyCheckBoxes = ArrayList<CheckBox>()

        var mask = 1
        for (x in 1..8) {
            val buttonHelpID = "btnHelp$x"
            val buttonHobbyID = "btnHobby$x"

            val resHelpID = resources.getIdentifier(buttonHelpID, "id", packageName)
            val resHobbyID = resources.getIdentifier(buttonHobbyID, "id", packageName)
            val helpBox: CheckBox = findViewById<View>(resHelpID) as CheckBox
            val hobbyBox: CheckBox = findViewById<View>(resHobbyID) as CheckBox

            if ((currUser.hobbies and mask) == mask) {
                hobbyBox.isChecked = true
            }
            hobbyBox.isEnabled = false
            hobbyBox.setTextColor(resources.getColor(R.color.colorBlack))
            hobbyCheckBoxes.add(hobbyBox)
            if ((currUser.help and mask) == mask) {
                helpBox.isChecked = true
            }
            if (!isMyUser) {
                helpBox.isEnabled = false
            }
            helpBox.setTextColor(resources.getColor(R.color.colorBlack))
            helpCheckBoxes.add(helpBox)
            mask = mask shl 1
        }
    }

    // Sets help for current user
    fun btnSetHelpOnClick(view: View) {
        var help = 0
        for ((i, checkBox) in helpCheckBoxes.withIndex()) {
            if (checkBox.isChecked) {
                help = help or (1 shl i)
            }
        }

        // needs to change help
        if (help == currUser.help) {
            LoginScreen.toastMessage(applicationContext, "Check/Uncheck boxes")
            return
        }

        val gson = GsonBuilder().setLenient().create()
        //Create the retrofit instance to issue with the network requests:
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

            //Defining the api for sending by the request
            val api = retrofit.create(Api::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                //Sending the request
                api.setHelp(currUser.userName, help).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.message() == "OK" || response.isSuccessful) {
                            LoginScreen.toastMessage(applicationContext, "changed help")
                        } else {
                            LoginScreen.toastMessage(applicationContext, "problem with changing the help")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println(t.message.toString())
                        LoginScreen.toastMessage(applicationContext, "Error in connection with server, go back to menu")
                    }
                })
            }
        } catch (e: Exception) {
            println(e.message.toString())
            LoginScreen.toastMessage(applicationContext, "Invalid url - Go back to menu");
        }
    }


}



