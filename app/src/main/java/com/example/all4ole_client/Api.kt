package com.example.all4ole_client

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    // GET  of PeopleLikeMe
    // example :     /user/people?username=”yosi"
    @GET("/user/people")
    fun peopleLikeMe(@Query("userName") userName:String): Call<ResponseBody>


    // POST of login
    // exmple: /user/login
    @Headers("Content-Type: application/json")
    @POST("user/login") //{password}
    fun logIn(@Body userDetails: RequestBody): Call<ResponseBody>
    //     fun ourMethodName we will call ( value we get : value we return )


    // POST of register
    //   exmple: /user/register

    // do like video with sending object of User !!! (vidao : 2:13)

}