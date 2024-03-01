package com.example.plmarket.media

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pl_market.R
import com.example.pl_market.databinding.ActivityMediatecaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediatecaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediatecaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatecaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            finish()
        }

binding.viewPager.adapter = MediatecaAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.selected_tracks)
                1 -> tab.text = getString(R.string.playList)
            }
        }
        tabMediator.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}
