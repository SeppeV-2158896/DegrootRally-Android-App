package be.seppevandenberk.degrootrally.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.ActivityMainBinding
import be.seppevandenberk.degrootrally.fragments.AccountFragment
import be.seppevandenberk.degrootrally.fragments.HoofdMenuFragment
import be.seppevandenberk.degrootrally.fragments.KalenderEnResultatenFragment
import be.seppevandenberk.degrootrally.model.ViewModelLoggedInUser
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val hoofdMenuFragment = HoofdMenuFragment()
    private val kalenderEnResultatenFragment = KalenderEnResultatenFragment()
    private val accountFragment = AccountFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val user = intent.getSerializableExtra("name") as String
        val loggedInUser = ViewModelProvider(this)[ViewModelLoggedInUser::class.java]
        loggedInUser.name.value = user

        displayFragment(hoofdMenuFragment)

        setupActionBarDrawer()

        setContentView(binding.root)
    }


    private fun setupActionBarDrawer() {
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close)
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)


        actionBarDrawerToggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("Navigation", "Item selected: ${item.itemId}")
        // we need to do this to respond correctly to clicks on menu items, otherwise it won't be caught
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.i("Navigation", "Item selected: ${item.itemId}")
        when (item.itemId) {
            R.id.nav_home -> {
                displayFragment(hoofdMenuFragment)
            }

            R.id.nav_kalender_en_resultaten -> {
                displayFragment(kalenderEnResultatenFragment)
            }

            R.id.nav_account -> {
                displayFragment(accountFragment)
            }

            R.id.nav_picture -> {
                navigateToPicture()
            }

            R.id.nav_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        // Close the navigation drawer if necessary
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun navigateToPicture() {
        TODO("Not yet implemented")
    }




    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentLayoutMain, fragment)
            addToBackStack(null)
            commit()
        }
    }




}