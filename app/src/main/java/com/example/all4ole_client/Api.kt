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


    // GET  of UserHelp
    // example :     /user/help?username=”yosi"&help=4
    @GET("/user/help")
    fun lookForHelp(@Query("userName") userName:String, @Query("help") help:Int): Call<ResponseBody>


    // GET  of findFriendsForHobbies
    // example :     /user/hobby?username=”yosi"&hobby=4
    @GET("/user/hobby")
    fun findFriendsForHobbies(@Query("userName") userName:String, @Query("hobbies") hobby:Int): Call<ResponseBody>


    // GET  of setHelp
    // example :     /user/hobby?username=”yosi"&help=4
    @GET("/user/setHelp")
    fun setHelp(@Query("userName") userName:String, @Query("help") help:Int): Call<ResponseBody>

    // POST of login
    // example: /user/login
    @Headers("Content-Type: application/json")
    @POST("user/login") //{password}
    fun logIn(@Body userDetails: RequestBody): Call<ResponseBody>


    // POST of register
    // example: /user/register
    @Headers("Content-Type: application/json")
    @POST("user/register") //{password}
    fun register(@Body userDetails: RequestBody): Call<ResponseBody>

}