package be.seppevandenberk.degrootrally.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.FragmentHoofdMenuBinding
import be.seppevandenberk.degrootrally.model.RallyAdapter
import be.seppevandenberk.degrootrally.model.RallyItem
import be.seppevandenberk.degrootrally.model.User
import be.seppevandenberk.degrootrally.model.ViewModelLoggedInUser
import be.seppevandenberk.degrootrally.repository.RallyItemsFileRepo
import java.util.Calendar

class HoofdMenuFragment : Fragment(R.layout.fragment_hoofd_menu) {
    private lateinit var binding: FragmentHoofdMenuBinding
    private val rallyItems = mutableListOf<RallyItem>()
    private val emptyRallyItem = RallyItem("No data provided.", "", "", Calendar.getInstance().time, "", "")
    private var rallyItemNextEvent = MutableList(1){emptyRallyItem}
    private var rallyItemLastResult = MutableList(1){emptyRallyItem}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rallyItemsFileRepo = this.context?.let { it1 -> RallyItemsFileRepo(it1) }
        if(rallyItemsFileRepo != null && rallyItemsFileRepo.read().size != rallyItems.size){
            rallyItems.addAll(rallyItemsFileRepo.read())
        }

        binding = FragmentHoofdMenuBinding.inflate(layoutInflater)

        val adapterNextEvent = RallyAdapter(rallyItemNextEvent)
        binding.recvwNextEventVw.adapter = adapterNextEvent
        binding.recvwNextEventVw.layoutManager = LinearLayoutManager(this.context)

        val adapterLastResult = RallyAdapter(rallyItemLastResult)
        binding.recvwLastResultVw.adapter = adapterLastResult
        binding.recvwLastResultVw.layoutManager = LinearLayoutManager(this.context)

        //Hiding the buttons on the main screen
        adapterLastResult.setEditDeleteButtonsVisible(false)
        adapterLastResult.setMapsButtonVisible(false)
        adapterNextEvent.setEditDeleteButtonsVisible(false)
        adapterNextEvent.setMapsButtonVisible(false)

        val user = ViewModelProvider(requireActivity())[ViewModelLoggedInUser::class.java]
        user.name.observe(viewLifecycleOwner){name ->
            val user = User(null,name,"",null,requireContext())
            val type = user.getType()
            if (type != "Admin"){
                binding.newsBodyTxtVw.setOnClickListener{
                    displayFragment(NewsFragment())
                }
            }
        }

        assignNextEventAndLastResultArray(rallyItems as ArrayList<RallyItem>)
        adapterLastResult.notifyDataSetChanged()

        binding.nextEventBtn.setOnClickListener {
            displayFragment(KalenderEnResultatenFragment())
        }
        binding.lastResultsBtn.setOnClickListener {
            displayFragment(KalenderEnResultatenFragment())
        }

        return binding.root
    }

    private fun sortRallyItemsByDate(rallyItems: ArrayList<RallyItem>): ArrayList<RallyItem>{
        if (rallyItems.size > 1){
            val sortedRallyItems = rallyItems
            sortedRallyItems.sortWith { rallyItem1, rallyItem2 ->
                rallyItem1.date.compareTo(rallyItem2.date)
            }
            return sortedRallyItems
        }
        return rallyItems
    }

    private fun assignNextEventAndLastResultArray(rallyItems: ArrayList<RallyItem>){
        val sortedRallyItems = sortRallyItemsByDate(rallyItems)

        rallyItemLastResult[0] = emptyRallyItem
        rallyItemNextEvent[0] = emptyRallyItem

        sortedRallyItems.forEachIndexed { index, rallyItem ->
            if (index < sortedRallyItems.size - 1 && rallyItem.date <= Calendar.getInstance().time && sortedRallyItems[index + 1].date >= Calendar.getInstance().time) {
                rallyItemLastResult[0] = rallyItem
                rallyItemNextEvent[0] = sortedRallyItems[index + 1]
                return
            }
            else if (index >= sortedRallyItems.size - 1 && Calendar.getInstance().time > rallyItem.date){
                rallyItemLastResult[0] = rallyItem
                rallyItemNextEvent[0] = RallyItem("No next event present.", "", "", Calendar.getInstance().time, "", "")
                return
            }
            else if (Calendar.getInstance().time < sortedRallyItems.first().date){
                rallyItemLastResult[0] = RallyItem("No results present.", "", "", Calendar.getInstance().time, "", "")
                rallyItemNextEvent[0] = rallyItem
                return
            }
        }
    }

    //refresh front page every time its opened so data is always up-to-date
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

        assignNextEventAndLastResultArray(rallyItems as ArrayList<RallyItem>)

        binding.recvwNextEventVw.adapter?.notifyDataSetChanged()
        binding.recvwLastResultVw.adapter?.notifyDataSetChanged()
    }

    private fun displayFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragmentLayoutMain, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}