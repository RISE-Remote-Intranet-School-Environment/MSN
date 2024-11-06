package org.schoolapp.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform