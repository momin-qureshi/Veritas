package com.melagroup.veritas.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.melagroup.veritas.data.LoginRepository
import com.melagroup.veritas.data.Result

import com.melagroup.veritas.R

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    val auth : FirebaseAuth = Firebase.auth

    fun checkLoggedIn(){
        if(auth.currentUser != null)
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = auth.currentUser?.displayName.toString()))
    }

    fun login(email: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _loginResult.value = LoginResult(success = LoggedInUserView(displayName = it.user?.displayName.toString()))

        }.addOnFailureListener{
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }

    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}