package com.example.all4ole_client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.edPassword
import kotlinx.android.synthetic.main.activity_main.edUserName
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
    //private var urlAddress: String? = "http://10.0.2.2:5001"  // for sending json
    //private var urlAddress: String? = "https://all4oleserver.azurewebsites.net" // for sending json
    private var urlAddress: String? = "http://3.138.60.124:5001" // for sending json



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.btnLogin)
//        registerButton = findViewById(R.id.tvRegister) // registerBtn

/*        btnNext.setOnClickListener{
            val intent=Intent(this,LoginCheck::class.java);
            intent.putExtra("data","test data")
            startActivity(intent)
        }*/
    }

    // log in - post
    private fun logIn(user_name: String, password: String) {
        val json = "{\n \"user_name\": \"${user_name}\",\n \"password\": \"${password}\"\n}"
        val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)
        val gson = GsonBuilder().setLenient().create()
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
                        if (response.message() == "OK" || response.isSuccessful) { // if ok we get all user ditels -how to get
                            // save details of curr user
                            println("Successfully posted - user logIn to app")
                           // val rate = Gson()
                            val userFromServer: User = gson.fromJson(response.body()?.string(), User::class.java)
                            println("trying to printttttt")
                            println(userFromServer.email + ", " + userFromServer.userName + ", " + userFromServer.password)
                            println("finished printttttt")
                            if(userFromServer.password == password){
                                currUser =  userFromServer
                                /*val intent = Intent(this@MainActivity,HomePageScreen::class.java)
                                intent.putExtra("currUser",currUser)
                                intent.putExtra("theUrl",urlAddress)
                                startActivity(intent)*/
                                val intent = Intent(this@MainActivity,ProfileDisplay::class.java)
                                intent.putExtra("currUser",currUser)
                                intent.putExtra("isMyUser",false)
                                intent.putExtra("theUrl",urlAddress)
                                startActivity(intent)
                            }
                        } else {
                            toastMessage(applicationContext,"userName or password is incorrect")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println(t.message.toString())
                        MainActivity.toastMessage(applicationContext, "Error in connection with server, go back to menu");
                    }
                })
            }
        } catch (e: Exception) {
            MainActivity.toastMessage(applicationContext, "Invalid url - Go back to menu");
        }
    }


    fun loginOnClick(view: View){
        usernameEditText = findViewById(R.id.edUserName)
        username = usernameEditText.text.toString()
        passwordEditText = findViewById(R.id.edPassword)
        password = passwordEditText.text.toString()
//            if(edUserName.text.trim().isNotEmpty() || edPassword.text.trim().isNotEmpty()){
        if(edUserName.text.isNotEmpty() && edPassword.text.isNotEmpty()){
            // do
            Toast.makeText(this,"input is ok",Toast.LENGTH_LONG).show()
            println("-------------------curr user name is: "+username+"-------------------" )
            println("-------------------curr password is: "+password+"---------------------" )
            Log.d("myTag", "This is my message");
        }
        // empty userName or password
        else{
            Toast.makeText(this,"input not full",Toast.LENGTH_LONG).show()
        }
        logIn(username, password)
    // TODO send username, password to server, if true go to homepage, else alert
    }

    //   move to registration screen
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