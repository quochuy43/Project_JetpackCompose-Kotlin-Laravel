package com.example.project_appmovie.database.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var fullName: String,
    var age: Int,
    var phoneNumber: Int,
    var email: String,
    var password: String,
    var avatar: String? = null
)
