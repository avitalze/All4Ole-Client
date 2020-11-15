package com.example.all4ole_client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login_check.*

//import android.support.v7.app.AppCompatActivity;


class LoginCheck : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_check)

        btnLogin.setOnClickListener(){
//            Toast.makeText(this,"clicked",Toast.LENGTH_SHORT.toShort())
            if(edUserName.text.trim().isNotEmpty() || edPassword.text.trim().isNotEmpty()){
                // do
                Toast.makeText(this,"input not full",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,"input is ok",Toast.LENGTH_LONG).show()
            }

        }

        // bring data
        val passData: String? =intent.getStringExtra("data");

        passData?.let { Log.e("pass data", it) };
    }
}