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
import kotlinx.android.synthetic.main.activity_login_screen.edPassword
import kotlinx.android.synthetic.main.activity_login_screen.edUserName
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


class LoginScreen : AppCompatActivity() {

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
    private lateinit var loginButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private var username: String = ""
    private var password: String = ""

    //private var urlAddress: String? = "http://10.0.2.2:5001"  // for sending json on my computer
    private var urlAddress: String? = "http://3.138.60.124:5001" // for sending json


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        loginButton = findViewById(R.id.btnLogin)
    }

    // log in - post to server
    private fun logIn(user_name: String, password: String) {
        val json = "{\n \"user_name\": \"${user_name}\",\n \"password\": \"${password}\"\n}"
        val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)
        val gson = GsonBuilder().setLenient().create()
        //Create the retrofit instance to issue with the network requests:
        try {
            val retrofit = Retrofit.Builder().baseUrl(urlAddress.toString())
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

            //Defining the api for sending by the request
            val api = retrofit.create(Api::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                //Sending the request
                api.logIn(rb).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        // if ok takes the user that came back from server (makes json a User) and saves details of curr user
                        if (response.message() == "OK" || response.isSuccessful) {
                            val userFromServer: User = gson.fromJson(response.body()?.string(), User::class.java)
                            if (userFromServer.password == password) {
                                currUser = userFromServer
                                val intent = Intent(this@LoginScreen, HomePageScreen::class.java)
                                intent.putExtra("currUser", currUser)
                                intent.putExtra("theUrl", urlAddress)
                                startActivity(intent)
                            }
                        } else {
                            toastMessage(applicationContext, "userName or password are incorrect")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println(t.message.toString())
                        toastMessage(applicationContext, "Error in connection with server, go back to menu");
                    }
                })
            }
        } catch (e: Exception) {
            toastMessage(applicationContext, "Invalid url - Go back to menu");
        }
    }

    // login to user if all is set
    fun loginOnClick(view: View) {
        usernameEditText = findViewById(R.id.edUserName)
        username = usernameEditText.text.toString()
        passwordEditText = findViewById(R.id.edPassword)
        password = passwordEditText.text.toString()
        if (edUserName.text.isNotEmpty() && edPassword.text.isNotEmpty()) {
            logIn(username, password)
        }
        // empty userName or password
        else {
            Toast.makeText(this, "input not full", Toast.LENGTH_LONG).show()
        }
    }

    //   move to registration screen
    fun registerOnClick(view: View) {
        val intent = Intent(this, RegistrationScreen::class.java)
        intent.putExtra("theUrl", urlAddress)
        startActivity(intent)
    }
}