package com.melagroup.veritas.ui.register

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.melagroup.veritas.MainActivity
import com.melagroup.veritas.R
import com.melagroup.veritas.ui.login.LoginViewModel
import com.melagroup.veritas.ui.login.afterTextChanged

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private var registerEnabled = false
    private lateinit var register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val name = findViewById<EditText>(R.id.fullname)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val confirm = findViewById<EditText>(R.id.confirm_password)
        register = findViewById<Button>(R.id.signup)
        val loading = findViewById<ProgressBar>(R.id.loading)

        register.isEnabled = false

        viewModel = ViewModelProvider(this, RegisterViewModelFactory())
                .get(RegisterViewModel::class.java)

        viewModel.registerFormState.observe(this@RegisterActivity, Observer{
            val formState = it ?: return@Observer
            if(formState.nameError != null) {
                name.error = getString(formState.nameError!!)
            }
            if(formState.emailError != null) {
                email.error = getString(formState.emailError!!)
            }
            if(formState.passwordError != null) {
                password.error = getString(formState.passwordError!!)
            }
            if(formState.confirmError != null) {
                confirm.error = getString(formState.confirmError!!)
            }
            changeRegisterState(formState.isDataValid)
        })

        viewModel.registerResult.observe(this@RegisterActivity, Observer{
            val result = it ?: return@Observer

            loading.visibility = View.GONE
            if (result.error != null) {
                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
            }
            if (result.success != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            setResult(Activity.RESULT_OK)
        })

        fun dataChanged(){
            viewModel.registerDataChanged(
                    name.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    confirm.text.toString()
            )
        }

        fun register(){
            if(registerEnabled) {
                loading.visibility = View.VISIBLE
                viewModel.register(
                        name.text.toString(),
                        email.text.toString(),
                        password.text.toString()
                )
            }
        }

        name.afterTextChanged { dataChanged() }
        email.afterTextChanged { dataChanged() }
        password.afterTextChanged { dataChanged() }
        confirm.apply{
            afterTextChanged { dataChanged() }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        register()
                }
                false
            }}
        register.setOnClickListener {
            register()
        }
    }

    private fun changeRegisterState(state: Boolean){
        registerEnabled = state
        register.isEnabled = state
    }
}