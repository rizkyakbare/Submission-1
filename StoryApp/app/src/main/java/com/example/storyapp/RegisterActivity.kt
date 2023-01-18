package com.example.storyapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.storyapp.data.Results
import com.example.storyapp.databinding.ActivityRegisterBinding
import com.example.storyapp.model.DefaultResponse
import com.example.storyapp.retrofit.ApiClient
import com.example.storyapp.util.LoadingDialog
import com.example.storyapp.util.showMessage
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var loading: LoadingDialog
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: RegisterViewModel by viewModels {
        factory
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    
        initLayout()
    }
    
    private fun initLayout(){
        loading = LoadingDialog(this)
        
        with(binding){
            binding.edtName.setOnFocusChangeListener{_, _ -> layoutName.error = null}
            binding.edtEmail.setOnFocusChangeListener{_, _ -> layoutEmail.error = null}
            
            btnLogin.setOnClickListener {
                finish()
            }
            
            btnRegister.setOnClickListener {
                val name = edtName.text.toString()
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                layoutName.error = viewModel.validateName(name)
                layoutEmail.error = viewModel.validateEmail(email)

                if(layoutName.error.isNullOrEmpty() && layoutEmail.error.isNullOrEmpty() && edtPassword.error.isNullOrEmpty()) {
                    viewModel.register(name, email, password).observe(this@RegisterActivity) {
                        when(it) {
                            is Results.Loading -> {
                                loading.show()
                            }
                            is Results.Success -> {
                                loading.dismiss()
                                showMessage(this@RegisterActivity, it.data.message)

                                if(!it.data.error) {
                                    finish()
                                }
                            }
                            is Results.Error -> {
                                loading.dismiss()
                                showMessage(this@RegisterActivity, it.error)
                            }
                        }
                    }
                } else {
                    showMessage(this@RegisterActivity, "Please review your input")
                }
            }
        }
    }
}