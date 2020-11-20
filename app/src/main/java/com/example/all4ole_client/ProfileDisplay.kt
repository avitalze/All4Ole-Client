package com.example.all4ole_client

import android.content.Intent
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
import okhttp3.MediaType
import okhttp3.RequestBody
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
    private lateinit var helpBtns: ArrayList<CheckBox>
    private lateinit var hobbyBtns: ArrayList<CheckBox>
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
        helpBtns = ArrayList<CheckBox>()
        hobbyBtns = ArrayList<CheckBox>()

        for (x in 1..8) {
            val buttonHelpID = "btnHelp$x"
            val buttonHobbyID = "btnHobby$x"

            val resHelpID = resources.getIdentifier(buttonHelpID, "id", packageName)
            val resHobbyID = resources.getIdentifier(buttonHobbyID, "id", packageName)
            helpBtns.add(findViewById<View>(resHelpID) as CheckBox)
            hobbyBtns.add(findViewById<View>(resHobbyID) as CheckBox)
        }

        //b.text = """Phone:${currUser.cell} """


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
        b = findViewById(R.id.txtStatus)
        var status: String? = null
        status = if (currUser.maritalStatus == 1) {
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
        /*  b = findViewById(R.id.txtPhone)
          b.text = applicationContext.getString(R.string.phone, currUser.cell)
          b = findViewById(R.id.txtPhone)
          b.text = applicationContext.getString(R.string.phone, currUser.cell)*/
    }

    fun btnSetHelpOnClick(view: View) {
        var help: Int = 0
        for ((i, checkBox) in helpBtns.withIndex()) {
            if (checkBox.isChecked) {
                help = help or (1 shl i)
            }
        }
        if (help != currUser.help) {
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
                    //TODO replace help by number that is generated by list the user choose from
                    api.lookForHelp(currUser.userName, help).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.message() == "OK" || response.isSuccessful) { // if ok we get all user ditels -how to get
                                MainActivity.toastMessage(applicationContext, "changed help")
                            } else {
                                MainActivity.toastMessage(applicationContext, "problem with changing the help")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            println(t.message.toString())
                            MainActivity.toastMessage(applicationContext, "Error in connection with server, go back to menu")
                        }
                    })
                }
            } catch (e: Exception) {
                println(e.message.toString())
                MainActivity.toastMessage(applicationContext, "Invalid url - Go back to menu");
            }
        }
    }


}



