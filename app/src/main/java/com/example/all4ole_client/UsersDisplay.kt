package com.example.all4ole_client

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_users_display.*

// https://www.youtube.com/watch?v=ai9rSGcDhyQ&ab_channel=CodePalace - RecyclerView Tutorial

class UsersDisplay : AppCompatActivity() {

    private var titlesList = mutableListOf<String>()
    private var descriptionsList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()
    private var users = mutableListOf<User>()

    // did recycler view to show the users as a dynamic list
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_display)
        users = (intent.getParcelableArrayListExtra("usersList"))!!
        postToList()

        rv_recyclerView.layoutManager = LinearLayoutManager(this)
        rv_recyclerView.adapter = RecyclerAdapter(titlesList, descriptionsList, imagesList,users,this@UsersDisplay)
    }

    //add record (user) to the list
    private fun addToList(title: String, description: String, image: Int, user:User){
        titlesList.add(title)
        descriptionsList.add(description)
        imagesList.add(image)
        users.add(user)
    }

    private fun postToList(){
        for (i in 0 until users.size){
            addToList(users[i].firstName + " " + users[i].lastName, "From " + users[i].originCountry, R.mipmap.ic_launcher_round, users[i])
        }
    }

}