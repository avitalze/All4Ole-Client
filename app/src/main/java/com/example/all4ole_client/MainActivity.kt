package com.example.all4ole_client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
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


class MainActivity :AppCompatActivity () {

    companion object {

        //toast message to the screen
        fun toastMessage(context: Context, messageText: String) {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, messageText, duration)

            toast.setGravity(Gravity.BOTTOM or Gravity.START, 100, 250)
            toast.show()
        }
    } // Global variables

    //variables
    private lateinit var currUser: User
    private var insertedUsername: String = "";
    private var insertedPassword: String = "";
    private var checkGit: String = "";

    // israel
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText:EditText
    private var username = ""
    private var password = ""
    private var urlAddress: String? = null  // for sending json



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.loginBtn)
        registerButton = findViewById(R.id.registerBtn)

        btnNext.setOnClickListener{
            val intent=Intent(this,LoginCheck::class.java);
            intent.putExtra("data","test data")
            startActivity(intent)
        }
    }

    // log in - post
    private fun logIn(user_name: String, password: String) {
        val json = "{\"username\": ${currUser.userName},\n \"password\": ${currUser.password},\n}"
        val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)
        val gson = GsonBuilder().setLenient().create();
        var currUserString=""
        //Create the retrofit instance to issue with the network requests:
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(urlAddress.toString())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();


            //Defining the api for sending by the request
            val api = retrofit.create(Api::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                //Sending the request
                api.logIn(rb).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.message() == "OK" || response.isSuccessful()) { // if ok we get all user ditels -how to get
                            // save details of curr user
                            println("Successfully posted - user logIn to app")
                            currUserString =response.body().toString(); // ?
                            response.body().use { model ->  } // check !

                            println(json)
                        } else {
                            MainActivity.toastMessage(
                                applicationContext,
                                "userName or password is incorrect"
                            )
                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        MainActivity.toastMessage(applicationContext, "Error in connection with server, go back to menu");
                    }
                })
            }
        } catch (e: Exception) {
            MainActivity.toastMessage(applicationContext, "Invalid url - Go back to menu");
        }



    }

//    israel old !
    fun loginOnClick(view: View){
        usernameEditText = findViewById(R.id.usernameEditText)
        username = usernameEditText.text.toString()
        passwordEditText = findViewById(R.id.passwordEditText)
        password = passwordEditText.text.toString()

        // TODO send username, password to server, if true go to homepage, else alert
    }

    //    israel old !
    fun registerOnClick(view: View){
        val intent = Intent(this, RegistrationScreen::class.java)
        startActivity(intent)
    }

    // POST login
//    fun clickOnLogInButton() {
//
//        // data class UserDitels ????
//        val json = "{\"username\": $username,\n \"password\": $password,\n}"
//        val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)
//        val gson = GsonBuilder().setLenient().create();
//        // use API func : logIn(rb)
//
//    }
}