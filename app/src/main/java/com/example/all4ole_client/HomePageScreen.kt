package com.example.all4ole_client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
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




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_screen)
        //currUser = user from last screen
        url = intent.getStringExtra("theUrl")!!
        peopleBtn = findViewById(R.id.btnPeople)
        currUser = intent.getParcelableExtra("currUser")!!

    }






    fun btnPeopleOnClick(view: View){
       val gson = GsonBuilder().setLenient().create()

        //val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)*/

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
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.message() == "OK" || response.isSuccessful) { // if ok we get all user ditels -how to get
                            // save details of curr user
                            println("Successfully got users!!!")
                            // val rate = Gson()
                            //val gson = GsonBuilder().setLenient().create()
                            val users: List<User> = gson.fromJson(response.body()?.string(), Array<User>::class.java).asList()
                            //val userrr: List<User> = gson.fromJson(response.body()?.string(), List<User>::class.java)
                            println("trying to printttttt")
                            for (i in users){
                                println(i.email + ", " + i.userName + ", " + i.password)
                            }
                            println("finished printttttt")

                            // todo show people in other screen
                        } else {
                            MainActivity.toastMessage(
                                applicationContext,
                                "userName or password is incorrect"
                            )
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println(t.message.toString())
                        MainActivity.toastMessage(applicationContext, "Error in connection with server, go back to menu");
                    }
                })
            }
        } catch (e: Exception) {
            println(e.message.toString())
            MainActivity.toastMessage(applicationContext, "Invalid url - Go back to menu");
        }
    }






}