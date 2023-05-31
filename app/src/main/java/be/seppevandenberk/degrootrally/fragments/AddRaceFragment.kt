package be.seppevandenberk.degrootrally.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.FragmentAddRaceBinding
import be.seppevandenberk.degrootrally.databinding.FragmentKalenderEnResultatenBinding
import be.seppevandenberk.degrootrally.model.RallyAdapter
import be.seppevandenberk.degrootrally.model.RallyItem
import be.seppevandenberk.degrootrally.repository.RallyItemsFileRepo
import java.math.BigDecimal
import java.util.*
import kotlin.collections.ArrayList


class AddRaceFragment : Fragment(R.layout.fragment_add_race) {
    val rallyItems = mutableListOf<RallyItem>()
    private lateinit var binding: FragmentAddRaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddRaceBinding.inflate(layoutInflater)

        val rallyItemsFileRepo = this.context?.let { it1 -> RallyItemsFileRepo(it1) }
        if(rallyItemsFileRepo != null && rallyItemsFileRepo.read().size != rallyItems.size){
            rallyItems.addAll(rallyItemsFileRepo.read())
        }

        var adapter = RallyAdapter(rallyItems)
        binding.addRaceBtn.setOnClickListener{
            var result: BigDecimal = try{
                BigDecimal(binding.txtResultEd.text.toString())
            } catch (e: Exception){
                BigDecimal.ZERO
            }
            rallyItems.add(RallyItem(binding.titleTxtEd.text.toString(), binding.pilotTxtEd.text.toString(), binding.copilotTxtEd.text.toString(), Calendar.getInstance().time, result))
            adapter.notifyItemInserted(rallyItems.size - 1)
            if (rallyItemsFileRepo != null) {
                rallyItemsFileRepo.save(rallyItems as ArrayList<RallyItem>)
            }
            displayFragment(KalenderEnResultatenFragment())
        }

        binding.dateTxtVw.setOnClickListener {
            //hier iets met kalender nog maken
        }

        return binding.root
    }

    fun displayFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragmentLayoutMain, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}