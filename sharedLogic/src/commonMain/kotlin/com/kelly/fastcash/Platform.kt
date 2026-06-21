package com.kelly.fastcash

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform