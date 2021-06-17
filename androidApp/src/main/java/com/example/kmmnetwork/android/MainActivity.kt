package com.example.kmmnetwork.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.kmmnetwork.Fetcher
import android.widget.TextView
import com.example.kmmnetwork.android.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val fetcher = Fetcher()
    private val mainScope = MainScope()

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.resultText.text = "<No Result>"
    }

    fun beginSearch(view: View) {
        val keyword = binding.searchText.text
        if (keyword.isNullOrEmpty()) return

        binding.resultText.text = "Loading..."
        mainScope.launch {
            kotlin.runCatching {
                fetcher.fetch(keyword.toString())
            }.onSuccess {
                binding.resultText.text = it
            }.onFailure {
                binding.resultText.text = "Error: ${it.localizedMessage}"
            }
        }
    }
}
