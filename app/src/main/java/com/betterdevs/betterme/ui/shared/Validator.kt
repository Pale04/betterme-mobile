package com.betterdevs.betterme.ui.shared

object Validator {
    fun isEmailCorrect(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@(.+)\$")
        return regex.matches(email)
    }

    fun isPasswordCorrect(password: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&_.])[A-Za-z\\d@\$!%*?&_.]{8,}$")
        return regex.matches(password)
    }
}

