package com.melagroup.veritas.ui.register

import com.melagroup.veritas.ui.login.LoggedInUserView

data class RegisterResult (
    val success: LoggedInUserView? = null,
    val error: Int? = null
)
