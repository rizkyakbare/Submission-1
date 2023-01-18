package com.example.storyapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.data.Results
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.model.DefaultResponse
import com.example.storyapp.model.LoginResponse
import com.example.storyapp.retrofit.ApiClient
import com.example.storyapp.util.LoadingDialog
import com.example.storyapp.util.SHARED_PREF_NAME
import com.example.storyapp.util.TOKEN
import com.example.storyapp.util.showMessage
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loading: LoadingDialog
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: LoginViewModel by viewModels{
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initLayout()
    }
    
    private fun initLayout(){
        loading = LoadingDialog(this)
        
        with(binding){
            binding.edtEmail.setOnFocusChangeListener{_, _ -> layoutEmail.error = null}
//            binding.edtPassword.setOnFocusChangeListener{_, _ -> layoutPassword.error = null}
            
            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            
            btnLogin.setOnClickListener {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                layoutEmail.error = viewModel.validateEmail(email)

                if(layoutEmail.error.isNullOrEmpty() && binding.edtPassword.error.isNullOrEmpty()) {
                    viewModel.login(email, password).observe(this@LoginActivity) {
                        when(it) {
                            is Results.Loading -> {
                                loading.show()
                            }
                            is Results.Success -> {
                                loading.dismiss()
                                showMessage(this@LoginActivity, it.data.message)

                                if(!it.data.error) {
                                    saveToken(it.data.result.token)
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    finish()
                                }
                            }
                            is Results.Error -> {
                                loading.dismiss()
                                showMessage(this@LoginActivity, it.error)
                            }
                        }
                    }
                } else {
                    showMessage(this@LoginActivity, "Please review your input")
                }
            }
        }
    }

    private fun saveToken(token: String){
        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString(TOKEN, token)
        editor.apply()
    }
}