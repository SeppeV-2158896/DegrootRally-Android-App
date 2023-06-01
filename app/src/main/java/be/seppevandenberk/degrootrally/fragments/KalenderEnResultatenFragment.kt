package be.seppevandenberk.degrootrally.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.FragmentKalenderEnResultatenBinding
import be.seppevandenberk.degrootrally.model.RallyAdapter
import be.seppevandenberk.degrootrally.model.RallyItem
import be.seppevandenberk.degrootrally.model.User
import be.seppevandenberk.degrootrally.model.ViewModelLoggedInUser
import be.seppevandenberk.degrootrally.repository.RallyItemsFileRepo
import com.google.android.material.snackbar.Snackbar
import java.time.Duration

class KalenderEnResultatenFragment : Fragment(R.layout.fragment_kalender_en_resultaten) {
    val rallyItems = mutableListOf<RallyItem>()
    private lateinit var binding: FragmentKalenderEnResultatenBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKalenderEnResultatenBinding.inflate(layoutInflater)

        val rallyItemsFileRepo = this.context?.let { it1 -> RallyItemsFileRepo(it1) }
        if(rallyItemsFileRepo != null && rallyItemsFileRepo.read().size != rallyItems.size){
            rallyItems.addAll(rallyItemsFileRepo.read())
        }

        var sortedRallyItems = sortRallyItemsByDate(rallyItems as ArrayList<RallyItem>)

        var adapter = RallyAdapter(sortedRallyItems)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        adapter.setEditDeleteButtonsVisible(true)

        val user = ViewModelProvider(requireActivity()).get(ViewModelLoggedInUser::class.java)
        user.name.observe(viewLifecycleOwner){name ->
            val user = User(null,name,"",null,requireContext())
            val type = user.getType()
            if (type != "Admin"){
                binding.addButton.visibility = View.INVISIBLE
                binding.deleteBtn.visibility = View.INVISIBLE
                adapter.setEditDeleteButtonsVisible(false)
            }
        }

        binding.addButton.setOnClickListener{
            displayFragment(AddRaceFragment())
        }

        binding.deleteBtn.setOnClickListener {
            if (rallyItemsFileRepo != null) {
                rallyItemsFileRepo.delete()
            }
            rallyItems.clear()
            adapter.notifyDataSetChanged()
        }

        adapter.setOnItemClickListener(object : RallyAdapter.OnItemClickListener{
            override fun onDeleteClick(position: Int) {
                sortedRallyItems.removeAt(position)
                if (rallyItemsFileRepo != null) {
                    rallyItemsFileRepo.delete()
                    rallyItemsFileRepo.save(sortedRallyItems)
                }
                adapter.notifyItemRemoved(position)
            }

            override fun onEditClick(position: Int) {
                val args = Bundle()
                args.putString("title", rallyItems[position].title)
                args.putLong("date", rallyItems[position].date.time)
                args.putString("pilot", rallyItems[position].piloot)
                args.putString("copilot", rallyItems[position].copiloot)
                args.putString("result", rallyItems[position].result)
                args.putInt("position", position)
                val fragment = AddRaceFragment()
                fragment.arguments = args
                /*
                rallyItems.removeAt(position)

                if (rallyItemsFileRepo != null) {
                    rallyItemsFileRepo.delete()
                    rallyItemsFileRepo.save(rallyItems)
                }
                */
                displayFragment(fragment)
            }
        })

        return binding.root
    }
    fun displayFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragmentLayoutMain, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun sortRallyItemsByDate(rallyItems: ArrayList<RallyItem>): ArrayList<RallyItem>{
        if (rallyItems.size > 1){
            var sortedRallyItems = rallyItems
            sortedRallyItems.sortWith(Comparator{rallyItem1, rallyItem2 ->
                rallyItem1.date.compareTo(rallyItem2.date)
            })
            return sortedRallyItems
        }
        return rallyItems
    }

    override fun onResume() {
        super.onResume()
        refreshMenu()
    }

    private fun refreshMenu(){
        rallyItems.clear()

        val rallyItemsFileRepo = this.context?.let { it1 -> RallyItemsFileRepo(it1) }
        if (rallyItemsFileRepo != null) {
            rallyItems.addAll(rallyItemsFileRepo.read())
        }

        var sortedRallyItems = sortRallyItemsByDate(rallyItems as ArrayList<RallyItem>)

        var adapter = RallyAdapter(sortedRallyItems)
        adapter.notifyDataSetChanged()
    }
}