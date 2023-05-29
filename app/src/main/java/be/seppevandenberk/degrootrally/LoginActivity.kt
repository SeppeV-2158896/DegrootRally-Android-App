package be.seppevandenberk.degrootrally

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import be.seppevandenberk.degrootrally.Fragments.ForgotPasswordFragment
import be.seppevandenberk.degrootrally.Fragments.LoginFragment
import be.seppevandenberk.degrootrally.Fragments.SignUpFragment
import be.seppevandenberk.degrootrally.databinding.ActivityLoginBinding
import be.seppevandenberk.degrootrally.databinding.ActivityMainBinding

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
