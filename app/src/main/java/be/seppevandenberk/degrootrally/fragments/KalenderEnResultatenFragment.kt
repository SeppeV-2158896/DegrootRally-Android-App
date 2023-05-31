package be.seppevandenberk.degrootrally.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.FragmentKalenderEnResultatenBinding
import be.seppevandenberk.degrootrally.model.RallyAdapter
import be.seppevandenberk.degrootrally.model.RallyItem
import be.seppevandenberk.degrootrally.repository.RallyItemsFileRepo

class KalenderEnResultatenFragment : Fragment(R.layout.fragment_kalender_en_resultaten) {
    val rallyItems = mutableListOf<RallyItem>()
    private lateinit var binding: FragmentKalenderEnResultatenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rallyItemsFileRepo = this.context?.let { it1 -> RallyItemsFileRepo(it1) }
        if(rallyItemsFileRepo != null && rallyItemsFileRepo.read().size != rallyItems.size){
            rallyItems.addAll(rallyItemsFileRepo.read())
        }

        binding = FragmentKalenderEnResultatenBinding.inflate(layoutInflater)
        var adapter = RallyAdapter(rallyItems)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        //val navController = NavHostFragment.findNavController(this)
        binding.addButton.setOnClickListener{
            findNavController().navigate(R.id.action_kalenderEnResultatenFragment_to_addRaceFragment, null)
        }
        return binding.root
    }
}