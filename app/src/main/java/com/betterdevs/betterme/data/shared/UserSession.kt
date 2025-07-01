package com.betterdevs.betterme.data.shared

object UserSession {
    private var id: String = ""
    private var username: String = ""
    private var role: String = ""
    private var email: String = ""

    fun setSession(id: String, username: String, role: String, email: String) {
        this.id = id
        this.username = username
        this.role = role
        this.email = email
    }

    fun isMember() = role == "Member"

    fun getId() = id

    fun getUsername() = username

    fun getEmail() = email
}