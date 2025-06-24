package com.betterdevs.betterme.data.shared

object UserSession {
    private var id: String = ""
    private var username: String = ""
    private var role: String = ""

    fun setSession(id: String, username: String, role: String) {
        this.id = id
        this.username = username
        this.role = role
    }

    fun isMember() = role == "Member"

    fun getId() = id
}