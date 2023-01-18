package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.model.Story

class DetailStoryActivity : AppCompatActivity() {
    companion object{
        const val EXTRA = "story"
    }
    
    private lateinit var binding: ActivityDetailStoryBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val story = intent.getParcelableExtra<Story>(EXTRA)
        
        binding.btnBack.setOnClickListener { finish() }
        
        story?.let {
            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.ivStory)
            
            binding.tvDesc.text = it.description
        }
    }
}