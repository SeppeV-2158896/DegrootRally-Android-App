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
import be.seppevandenberk.degrootrally.databinding.FragmentKalenderEnResultatenBinding
import be.seppevandenberk.degrootrally.model.RallyAdapter
import be.seppevandenberk.degrootrally.model.RallyItem
import be.seppevandenberk.degrootrally.model.User
import be.seppevandenberk.degrootrally.model.ViewModelLoggedInUser
import be.seppevandenberk.degrootrally.repository.RallyItemsFileRepo
import java.math.BigDecimal
import java.util.Calendar
import java.util.Date

class HoofdMenuFragment : Fragment(R.layout.fragment_hoofd_menu) {
    //Dit stuk code is om de json opslag file te kunnen deleten vanuit een andere fragment voor
    // als er iets misloopt en we niet meer in de fragment met de delete knop geraken. ->
    private lateinit var binding: FragmentHoofdMenuBinding
    val rallyItems = mutableListOf<RallyItem>()
    var rallyItemNextEvent = mutableListOf<RallyItem>()
    var rallyItemLastResult = mutableListOf<RallyItem>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rallyItemsFileRepo = this.context?.let { it1 -> RallyItemsFileRepo(it1) }
        if(rallyItemsFileRepo != null && rallyItemsFileRepo.read().size != rallyItems.size){
            rallyItems.addAll(rallyItemsFileRepo.read())
        }
        binding = FragmentHoofdMenuBinding.inflate(layoutInflater)

        var adapterNextEvent = RallyAdapter(rallyItemNextEvent)
        binding.recvwNextEventVw.adapter = adapterNextEvent
        binding.recvwNextEventVw.layoutManager = LinearLayoutManager(this.context)

        var adapterLastResult = RallyAdapter(rallyItemLastResult)
        binding.recvwLastResultVw.adapter = adapterLastResult
        binding.recvwLastResultVw.layoutManager = LinearLayoutManager(this.context)

        adapterLastResult.setEditDeleteButtonsVisible(false)
        adapterNextEvent.setEditDeleteButtonsVisible(false)

        val user = ViewModelProvider(requireActivity()).get(ViewModelLoggedInUser::class.java)
        user.name.observe(viewLifecycleOwner){name ->
            val user = User(null,name,"",null,requireContext())
            val type = user.getType()
            if (type != "Admin"){
                binding.newsBodyTxtVw.setOnClickListener{
                    val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                    transaction.replace(R.id.fragmentLayoutLogin, NewsFragment())
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
            }
        }

        assignNextEventAndLastResultArray(rallyItems as ArrayList<RallyItem>)
        adapterLastResult.notifyDataSetChanged()

        return binding.root
    }
    // -> tot hier
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

    fun assignNextEventAndLastResultArray(rallyItems: ArrayList<RallyItem>){
        var sortedRallyItems = sortRallyItemsByDate(rallyItems)

        val emptyRallyItem = RallyItem("No data provided.", "", "", Calendar.getInstance().time, "")

        rallyItemLastResult.add(emptyRallyItem)
        rallyItemNextEvent.add(emptyRallyItem)

        sortedRallyItems.forEachIndexed { index, rallyItem ->
            if (index < sortedRallyItems.size - 1 && sortedRallyItems[index + 1].date >= Calendar.getInstance().time) {
                rallyItemLastResult[0] = rallyItem
                rallyItemNextEvent[0] = sortedRallyItems[index + 1]
                return
            }
            else if (index >= sortedRallyItems.size - 1 && Calendar.getInstance().time > rallyItem.date){
                rallyItemLastResult[0] = rallyItem
                rallyItemNextEvent[0] = RallyItem("No next event present.", "", "", Calendar.getInstance().time, "")
                return
            }
            else{
                rallyItemLastResult[0] = RallyItem("No next event present.", "", "", Calendar.getInstance().time, "")
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
}