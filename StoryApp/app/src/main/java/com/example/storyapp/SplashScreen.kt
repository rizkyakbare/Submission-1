package com.example.storyapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.storyapp.databinding.ActivitySplashScreenBinding
import com.example.storyapp.util.SHARED_PREF_NAME
import com.example.storyapp.util.TOKEN


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val token = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).getString(TOKEN, "")
        
        val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, binding.appName, "transition_text_logo")
        
        Handler().postDelayed({
            if(token.isNullOrEmpty()) {
                startActivity(Intent(this@SplashScreen, LoginActivity::class.java), activityOptionsCompat.toBundle())
                
            } else {
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            }
            
            finish()
        }, 2000)
    }
}