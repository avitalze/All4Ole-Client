package com.example.all4ole_client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
//import android.support.v7.app.AppCompatActivity;


class LoginCheck : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_check)

        val passData: String? =intent.getStringExtra("data");

        passData?.let { Log.e("pass data", it) };
    }
}