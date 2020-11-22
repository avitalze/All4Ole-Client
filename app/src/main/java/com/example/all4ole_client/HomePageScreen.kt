package com.example.all4ole_client

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

class HomePageScreen : AppCompatActivity() {

    private lateinit var currUser: User
    private lateinit var url: String
    private lateinit var peopleBtn: Button
    private lateinit var helpCheckBoxes: ArrayList<CheckBox>
    private lateinit var hobbyCheckBoxes: ArrayList<CheckBox>
    private lateinit var welcomeTxt: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_screen)
        //currUser = user from last screen
        url = intent.getStringExtra("theUrl")!!
        peopleBtn = findViewById(R.id.btnPeople)

        //Put checkboxes in lists (hobbies and help)
        helpCheckBoxes = ArrayList<CheckBox>()
        hobbyCheckBoxes = ArrayList<CheckBox>()
        addCheckBoxes()
        welcomeTxt = findViewById(R.id.welcomeTextView)
        currUser = intent.getParcelableExtra("currUser")!!
        //Put "welcome firstName at top of screen"
        val welcomeUser: String = applicationContext.getString(R.string.welcome_home, currUser.firstName)
        welcomeTxt.text = welcomeUser
    }

    // Add check boxes to lists
    private fun addCheckBoxes() {
        for (x in 1..8) {
            val buttonHelpID = "btnHelp$x"
            val buttonHobbyID = "btnHobby$x"
            val resHelpID = resources.getIdentifier(buttonHelpID, "id", packageName)
            val resHobbyID = resources.getIdentifier(buttonHobbyID, "id", packageName)
            val helpBox: CheckBox = findViewById<View>(resHelpID) as CheckBox
            val hobbyBox: CheckBox = findViewById<View>(resHobbyID) as CheckBox
            hobbyBox.setTextColor(resources.getColor(R.color.colorBlack))
            helpBox.setTextColor(resources.getColor(R.color.colorBlack))
            hobbyCheckBoxes.add(hobbyBox)
            helpCheckBoxes.add(helpBox)
        }
    }

    // Goes back to login if disconnect button is pressed
    fun btnDisconnectOnClick(view: View) {
        finish()
    }

    // Goes to profile display when user presses "My Profile"
    fun btnProfileOnClick(view: View) {
        val intent = Intent(this@HomePageScreen, ProfileDisplay::class.java)
        intent.putExtra("currUser", currUser)
        intent.putExtra("isMyUser", true)
        intent.putExtra("theUrl", url)
        startActivity(intent)
    }

    // If user presses on help
    fun btnHelpOnClick(view: View) {
        var help: Int = 0
        for ((i, checkBox) in helpCheckBoxes.withIndex()) {
            if (checkBox.isChecked) {
                help = help or (1 shl i)
            }
        }

        // need at least one topic for help
        if (help == 0) {
            LoginScreen.toastMessage(applicationContext, "Check at least one!")
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
                api.lookForHelp(currUser.userName, help).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.message() == "OK" || response.isSuccessful) {
                            // get users from server
                            val users: ArrayList<User> = gson.fromJson(response.body()?.string(), Array<User>::class.java)
                                .asList().toArrayList()

                            // Go to users display with all users matched (from server)
                            val intent = Intent(this@HomePageScreen, UsersDisplay::class.java)
                            intent.putParcelableArrayListExtra("usersList", users)
                            startActivity(intent)
                        } else {
                            LoginScreen.toastMessage(applicationContext, "userName or password is incorrect")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println(t.message.toString())
                        LoginScreen.toastMessage(applicationContext, "Error in connection with server\n go back to menu")
                    }
                })
            }
        } catch (e: Exception) {
            println(e.message.toString())
            LoginScreen.toastMessage(applicationContext, "Invalid url - Go back to menu");
        }

    }

    // make list an array list
    fun <T> List<T>.toArrayList(): ArrayList<T> {
        return ArrayList(this)
    }

    // Get users who likes doing my hobbies
    fun btnHobbyOnClick(view: View) {
        var hobby: Int = 0
        for ((i, checkBox) in hobbyCheckBoxes.withIndex()) {
            if (checkBox.isChecked) {
                hobby = hobby or (1 shl i)
            }
        }

        // need at least one hobby
        if (hobby == 0) {
            LoginScreen.toastMessage(applicationContext, "Check at least one!")
            return
        }

        val gson = GsonBuilder().setLenient().create()
        //Create the retrofit instance to issue with the network requests:
        try {
            val retrofit = Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()

            //Defining the api for sending by the request
            val api = retrofit.create(Api::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                //Sending the request
                api.findFriendsForHobbies(currUser.userName, hobby).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.message() == "OK" || response.isSuccessful) {
                            // save details of curr user
                            val users: ArrayList<User> = gson.fromJson(response.body()?.string(), Array<User>::class.java)
                                .asList().toArrayList()
                            //Go to users display (users matched from server)
                            val intent = Intent(this@HomePageScreen, UsersDisplay::class.java)
                            intent.putParcelableArrayListExtra("usersList", users)
                            startActivity(intent)
                        } else {
                            LoginScreen.toastMessage(applicationContext, "userName or password is incorrect")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println(t.message.toString())
                        LoginScreen.toastMessage(applicationContext, "Error in connection with server\n go back to menu")
                    }
                })
            }
        } catch (e: Exception) {
            println(e.message.toString())
            LoginScreen.toastMessage(applicationContext, "Invalid url - Go back to menu")
        }

    }

    // Search for people in the server that are similar to the user
    fun btnPeopleOnClick(view: View) {
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
                api.peopleLikeMe(currUser.userName).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.message() == "OK" || response.isSuccessful) {
                            val users: ArrayList<User> = gson.fromJson(response.body()?.string(), Array<User>::class.java)
                                .asList().toArrayList()
                            val intent = Intent(this@HomePageScreen, UsersDisplay::class.java)
                            intent.putParcelableArrayListExtra("usersList", users)
                            startActivity(intent)
                        } else {
                            LoginScreen.toastMessage(applicationContext, "userName or password is incorrect")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println(t.message.toString())
                        LoginScreen.toastMessage(applicationContext, "Error in connection with server\n go back to menu")
                    }
                })
            }
        } catch (e: Exception) {
            println(e.message.toString())
            LoginScreen.toastMessage(applicationContext, "Invalid url - Go back to menu")
        }
    }
}