package com.betterdevs.betterme.domain_model

class Response<T>(val success: Boolean, val message: String, val data: T? = null) {}