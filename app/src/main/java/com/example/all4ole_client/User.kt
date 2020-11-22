/*
package com.example.all4ole_client

class User {
    var userName : String=""
//    val age = 32
    var password : String=""
    var firstName : String=""
    var lastName : String=""
    var cell : String=""
    var hobbies : Int=0
    var help : Int=0
    var email : String=""
    var originCountry : String=""
    var language : String=""
    var residentialArea : String=""
    var maritalStatus : Int=0
    var hasLittleChildren : Int=0


    constructor(userName: String, _password: String, _firstName: String, lastName: String, _cell: String,
                _hobbies : Int, _help : Int, _email : String, _originCountry : String, _language : String,
                _residentialArea : String, _maritalStatus : Int, _hasLittleChildren : Int ) {
        this.userName = userName
        this.password = _password
        this.firstName=_firstName
        this.lastName =lastName
        this.cell =_cell
        this.hobbies =_hobbies
        this.help =_help
        this.email =_email
        this.originCountry =_originCountry
        this.language =_language
        this.residentialArea =_residentialArea
        this.maritalStatus =_maritalStatus
        this.hasLittleChildren=_hasLittleChildren

        println("$userName: $password created")
    }

    // empty
    //sskksk
    constructor(){
        this.userName = ""
        this.password = ""
        this.firstName= ""
        this.lastName =""
        this.cell= ""
        this.hobbies =0
        this.help =0
        this.email =""
        this.originCountry =""
        this.language =""
        this.residentialArea =""
        this.maritalStatus =0
        this.hasLittleChildren=0
    }
}*/
package com.example.all4ole_client

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    @SerializedName("cell")
    var cell: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("has_little_children")
    var hasLittleChildren: Int,
    @SerializedName("help")
    var help: Int,
    @SerializedName("hobbies")
    var hobbies: Int,
    @SerializedName("language")
    var language: String,
    @SerializedName("last_name")
    var lastName: String,
    @SerializedName("marital_status")
    var maritalStatus: Int,
    @SerializedName("origin_country")
    var originCountry: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("residential_area")
    var residentialArea: String,
    @SerializedName("user_name")
    val userName: String
)  : Parcelable
