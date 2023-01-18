package com.example.storyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.storyapp.adapter.LoadingStateAdapter
import com.example.storyapp.adapter.StoryAdapter
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.util.LoadingDialog
import com.example.storyapp.util.SHARED_PREF_NAME
import com.example.storyapp.util.TOKEN

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var loading: LoadingDialog
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: MainViewModel by viewModels {
        factory
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initLayout()
    }
    
    private fun initLayout(){
        loading = LoadingDialog(this)
        val lm = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        lm.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        with(binding){
            rvStory.hasFixedSize()
            rvStory.layoutManager = lm
            
            swipeRefresh.setOnRefreshListener {
                getData()
            }
            
            fab.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
            }

            btnMaps.setOnClickListener { startActivity(Intent(this@MainActivity, MapsActivity::class.java)) }
            btnLogout.setOnClickListener { logout() }
        }
        
        getData()
    }
    
    private fun logout() {
        val sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        
        startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }
    
    private fun getData(){
        binding.swipeRefresh.isRefreshing = false
        
        val token = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE).getString(TOKEN, "")
        val auth = "Bearer $token"

        val adapter = StoryAdapter()

        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.getStories(auth).observe(this){
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }
}