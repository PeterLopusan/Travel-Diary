package com.peterlopusan.traveldiary.models.user

data class CreateUserBody(
    var firstname: String = "",
    var lastname: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = ""
)
