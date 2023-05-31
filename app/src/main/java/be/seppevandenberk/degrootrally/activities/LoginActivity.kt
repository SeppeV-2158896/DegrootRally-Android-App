package be.seppevandenberk.degrootrally.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.ActivityLoginBinding
import be.seppevandenberk.degrootrally.fragments.ForgotPasswordFragment
import be.seppevandenberk.degrootrally.fragments.LoginFragment
import be.seppevandenberk.degrootrally.fragments.SignUpFragment

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginFragment = LoginFragment()
    private val signUpFragment = SignUpFragment()
    private val forgotPasswordFragment = ForgotPasswordFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        displayFragment(loginFragment)

        setContentView(binding.root)
    }

    fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentLayoutLogin, fragment)
            addToBackStack(null)
            commit()
        }
    }
}
