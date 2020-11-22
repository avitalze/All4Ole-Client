package com.example.all4ole_client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_users_display.*
import java.lang.StringBuilder

// https://www.youtube.com/watch?v=ai9rSGcDhyQ&ab_channel=CodePalace - RecyclerView Tutorial

class UsersDisplay : AppCompatActivity() {

    private var titlesList = mutableListOf<String>()
    private var descriptionsList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()
    private var users = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_display)
        users = (intent.getParcelableArrayListExtra("usersList"))!!
        postToList()

        rv_recyclerView.layoutManager = LinearLayoutManager(this)
        rv_recyclerView.adapter = RecyclerAdapter(titlesList, descriptionsList, imagesList,users,this@UsersDisplay)
    }

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