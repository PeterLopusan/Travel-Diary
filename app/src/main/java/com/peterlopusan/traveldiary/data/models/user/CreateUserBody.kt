package com.peterlopusan.traveldiary.data.models.user

data class CreateUserBody(
    var firstname: String = "",
    var lastname: String = "",
    var email: String = "",
    var password: String = "",
    var confirmPassword: String = ""
)
