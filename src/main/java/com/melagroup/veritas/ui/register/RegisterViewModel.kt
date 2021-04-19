package com.melagroup.veritas.ui.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.melagroup.veritas.R
import com.melagroup.veritas.ui.login.LoggedInUserView
import com.melagroup.veritas.ui.login.LoginFormState
import com.melagroup.veritas.ui.login.LoginResult

class RegisterViewModel : ViewModel()  {

    private var _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    val auth : FirebaseAuth = Firebase.auth
    val db = Firebase.firestore

    fun register(name: String, email: String, password: String) {
        // can be launched in a separate asynchronous job
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener{
            auth.currentUser?.updateProfile(userProfileChangeRequest{
                displayName= name
            })?.addOnCompleteListener{
                val data = hashMapOf(
                        "uid" to auth.currentUser!!.uid,
                        "displayName" to name,
                        "email" to email
                )
                db.collection("users").document(data["uid"]!!).set(data).addOnSuccessListener{
                    _registerResult.value = RegisterResult(success = LoggedInUserView(displayName = auth.currentUser!!.displayName.toString()))
                }.addOnFailureListener {
                    _registerResult.value = RegisterResult(error = R.string.register_failed)
                }
            }?.addOnFailureListener{
                _registerResult.value = RegisterResult(error = R.string.register_failed)
            }
        }.addOnFailureListener{
            _registerResult.value = RegisterResult(error = R.string.register_failed)
        }
    }

    fun registerDataChanged(name: String, email: String, password: String, confirm: String) {

        val formState = RegisterFormState()
        var valid = true
        if (!isNameValid(name)) {
            formState.nameError = R.string.invalid_name
            valid = false
        }
        if (!isEmailValid(email)) {
            formState.emailError = R.string.invalid_email
            valid = false
        }
        if (!isPasswordValid(password)) {
            formState.passwordError = R.string.invalid_password
            valid = false
        }
        if (!passwordsMatch(password, confirm)) {
            formState.confirmError = R.string.invalid_confirm_password
            valid = false
        }
        if(valid){
            formState.isDataValid = true
        }
        _registerForm.value = formState
    }

    private fun isNameValid(name: String): Boolean{
        return name.isNotBlank()
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun passwordsMatch(password: String, confirm: String): Boolean {
        return password == confirm
    }
}