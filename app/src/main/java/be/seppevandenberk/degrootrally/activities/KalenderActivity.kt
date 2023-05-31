package be.seppevandenberk.degrootrally.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.ActivityKalenderBinding

class KalenderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKalenderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKalenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
    }
}