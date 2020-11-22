package com.example.all4ole_client

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView



class RecyclerAdapter (private var titles: List<String>, private var details: List<String>, private var images:List<Int>, private var
users:List<User>, private var theContext:Context):
RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemDetail: TextView = itemView.findViewById(R.id.tv_description)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)
        lateinit var itemUser: User
        lateinit var context:Context


        // if user pressed a record it takes him to the user display
        init {
            itemView.setOnClickListener{
                val intent = Intent(context,ProfileDisplay::class.java)
                intent.putExtra("currUser",itemUser)
                intent.putExtra("isMyUser",false)
                intent.putExtra("theUrl","")
                context.startActivity(intent)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v :View = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    // add fields to record in list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]
        holder.itemPicture.setImageResource(images[position])
        holder.itemUser = users[position]
        holder.context = theContext
    }



}