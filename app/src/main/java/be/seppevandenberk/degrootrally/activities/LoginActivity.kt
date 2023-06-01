package be.seppevandenberk.degrootrally.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.ActivityLoginBinding
import be.seppevandenberk.degrootrally.fragments.LoginFragment

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginFragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        displayFragment(loginFragment)

        setContentView(binding.root)
    }

    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentLayoutLogin, fragment)
            addToBackStack(null)
            commit()
        }
    }
}
