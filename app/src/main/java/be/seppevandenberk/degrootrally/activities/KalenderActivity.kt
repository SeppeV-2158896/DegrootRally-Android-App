package be.seppevandenberk.degrootrally.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.ActivityKalenderBinding
import be.seppevandenberk.degrootrally.fragments.KalenderEnResultatenFragment

class KalenderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKalenderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKalenderBinding.inflate(layoutInflater)

        displayFragment(KalenderEnResultatenFragment())

        setContentView(binding.root)
    }
    fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutKalender, fragment)
            addToBackStack(null)
            commit()
        }
    }
}