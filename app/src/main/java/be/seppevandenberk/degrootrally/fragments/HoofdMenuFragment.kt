package be.seppevandenberk.degrootrally.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.FragmentHoofdMenuBinding
import be.seppevandenberk.degrootrally.databinding.FragmentKalenderEnResultatenBinding
import be.seppevandenberk.degrootrally.model.RallyAdapter
import be.seppevandenberk.degrootrally.model.RallyItem
import be.seppevandenberk.degrootrally.model.User
import be.seppevandenberk.degrootrally.model.ViewModelLoggedInUser
import be.seppevandenberk.degrootrally.repository.RallyItemsFileRepo

class HoofdMenuFragment : Fragment(R.layout.fragment_hoofd_menu) {
    //Dit stuk code is om de json opslag file te kunnen deleten vanuit een andere fragment voor
    // als er iets misloopt en we niet meer in de fragment met de delete knop geraken. ->
    private lateinit var binding: FragmentHoofdMenuBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rallyItemsFileRepo = this.context?.let { it1 -> RallyItemsFileRepo(it1) }
        binding = FragmentHoofdMenuBinding.inflate(layoutInflater)
        binding.tempEmergencyBtn.setOnClickListener {
            if (rallyItemsFileRepo != null) {
                rallyItemsFileRepo.delete()
            }
        }
        return binding.root
    }
    // -> tot hier
}