package com.melagroup.veritas.ui.register

data class RegisterFormState(var nameError: Int? = null,
                             var emailError: Int? = null,
                             var passwordError: Int? = null,
                             var confirmError: Int? = null,
                             var isDataValid: Boolean = false)