package be.seppevandenberk.degrootrally.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.FragmentKalenderEnResultatenBinding
import be.seppevandenberk.degrootrally.model.RallyAdapter
import be.seppevandenberk.degrootrally.model.RallyItem
import be.seppevandenberk.degrootrally.model.User
import be.seppevandenberk.degrootrally.model.ViewModelLoggedInUser
import be.seppevandenberk.degrootrally.repository.RallyItemsFileRepo
import com.google.android.material.snackbar.Snackbar
import java.net.URLEncoder
import java.util.Date

class KalenderEnResultatenFragment : Fragment(R.layout.fragment_kalender_en_resultaten) {
    val rallyItems = mutableListOf<RallyItem>()
    var sortedRallyItems = mutableListOf<RallyItem>()
    private lateinit var binding: FragmentKalenderEnResultatenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKalenderEnResultatenBinding.inflate(layoutInflater)

        initializeArray(rallyItems)

        val rallyItemsFileRepo = this.context?.let { it1 -> RallyItemsFileRepo(it1) }
        if (rallyItemsFileRepo != null) {
            rallyItemsFileRepo.save(rallyItems as ArrayList<RallyItem>)
        }
        if (rallyItemsFileRepo != null && rallyItemsFileRepo.read().size != rallyItems.size) {
            rallyItems.addAll(rallyItemsFileRepo.read())
        }

        sortedRallyItems = sortRallyItemsByDate(rallyItems as ArrayList<RallyItem>)

        val adapter = RallyAdapter(sortedRallyItems)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)

        adapter.setEditDeleteButtonsVisible(true)

        val user = ViewModelProvider(requireActivity())[ViewModelLoggedInUser::class.java]
        user.name.observe(viewLifecycleOwner) { name ->
            val user = User(null, name, "", null, requireContext())
            val type = user.getType()
            if (type != "Admin") {
                binding.addButton.visibility = View.INVISIBLE
                adapter.setEditDeleteButtonsVisible(false)
            }
        }

        binding.addButton.setOnClickListener {
            displayFragment(AddRaceFragment())
        }

        adapter.setOnItemClickListener(object : RallyAdapter.OnItemClickListener {
            override fun onDeleteClick(position: Int) {
                if (sortedRallyItems.size - 1 < 1) {
                    val rootView = binding.root
                    Snackbar.make(
                        rootView,
                        "There must be at least one item in this list.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    sortedRallyItems.removeAt(position)
                    if (rallyItemsFileRepo != null) {
                        rallyItemsFileRepo.delete()
                        rallyItemsFileRepo.save(sortedRallyItems as ArrayList<RallyItem>)
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onEditClick(position: Int) {
                val args = Bundle()
                args.putString("title", rallyItems[position].title)
                args.putLong("date", rallyItems[position].date.time)
                args.putString("pilot", rallyItems[position].piloot)
                args.putString("copilot", rallyItems[position].copiloot)
                args.putString("result", rallyItems[position].result)
                args.putString("address", rallyItems[position].address)

                args.putInt("position", position)

                val fragment = AddRaceFragment()
                fragment.arguments = args
                displayFragment(fragment)
            }

            override fun onMapsClick(position: Int) {
                if (rallyItems[position].address.isNotBlank()) {
                    val address = rallyItems[position].address

                    val encodedAddress = URLEncoder.encode(address, "UTF-8")
                    val uri = "geo:0,0?q=$encodedAddress"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    intent.setPackage("com.google.android.apps.maps")

                    if (intent.resolveActivity(requireActivity().packageManager) != null) {
                        startActivity(intent)
                    }
                } else {
                    val rootView: View = binding.root
                    Snackbar.make(
                        rootView,
                        "No location present, please add a location.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
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

    private fun sortRallyItemsByDate(rallyItems: ArrayList<RallyItem>): ArrayList<RallyItem> {
        if (rallyItems.size > 1) {
            val sortedRallyItems = rallyItems
            sortedRallyItems.sortWith { rallyItem1, rallyItem2 ->
                rallyItem1.date.compareTo(rallyItem2.date)
            }
            return sortedRallyItems
        }
        return rallyItems
    }

    override fun onResume() {
        super.onResume()
        refreshMenu()
    }

    private fun refreshMenu() {
        rallyItems.clear()

        val rallyItemsFileRepo = this.context?.let { it1 -> RallyItemsFileRepo(it1) }
        if (rallyItemsFileRepo != null) {
            rallyItems.addAll(rallyItemsFileRepo.read())
        }

        sortedRallyItems = sortRallyItemsByDate(rallyItems as ArrayList<RallyItem>)

        val adapter = RallyAdapter(sortedRallyItems)
        adapter.notifyDataSetChanged()
    }

    fun initializeArray(rallyItems: MutableList<RallyItem>){
        val rallyVanHaspengouw2017 = RallyItem("Rally van Haspengouw 2017","Nico Degroot","Kenneth Dardenne",
            Date(2017-1900,2-1,27),"29","Lichtenberglaan 1090,3800 Sint-Truiden")
        rallyItems.add(rallyVanHaspengouw2017)
        val rallyeDeHannut2017 = RallyItem("Rallye De Hannut 2017","Nico Degroot","Mike van den Brink",
            Date(2017-1900,3-1,12),"32","Rue Lambert Mottart 21, 4280 Hannut")
        rallyItems.add(rallyeDeHannut2017)
        val sezoensrally2017 = RallyItem("Sezoensrally 2017","Nico Degroot","Kenneth Dardenne",
            Date(2017-1900,5-1,20),"21","Dorperheideweg 56, 3950 Bocholt, België")
        rallyItems.add(sezoensrally2017)
        val bouclesChevrotines2017 = RallyItem("Boucles Chevrotines 2017","Nico Degroot","Veerle Bovy",
            Date(2017-1900,8-1,6),"Retired","Rue de l’Ile Dossai 12, 5300 SCLAYN")
        rallyItems.add(bouclesChevrotines2017)
        val criteriumJeanLouisDumont2017 = RallyItem("Critérium Jean-Louis Dumont 2017","Nico Degroot","Nicky Vandepoel",
            Date(2017-1900,9-1,17),"15","Zoning industriel de Waremme 4300 Borgworm")
        rallyItems.add(criteriumJeanLouisDumont2017)
        val rallyVanZuidLimburg2017 = RallyItem("Rally van Zuid-Limburg 2017","Nico Degroot","Filip Keyen",
            Date(2017-1900,11-1,12),"24"," Ambachtsweg 5  - 3890 Gingelom")
        rallyItems.add(rallyVanZuidLimburg2017)
        val rallyVanHaspengouw2018 = RallyItem("Rally van Haspengouw 2018","Nico Degroot","Mike van den Brink",
            Date(2018-1900,2-1,24),"20","Lichtenberglaan 1090,3800 Sint-Truiden")
        rallyItems.add(rallyVanHaspengouw2018)
        val rallyeDeHannut2018 = RallyItem("Rallye De Hannut 2018","Nico Degroot","Thomas Driesen",
            Date(2018-1900,3-1,11),"Accident","Rue Lambert Mottart 21, 4280 Hannut")
        rallyItems.add(rallyeDeHannut2018)
        val rallyVanZuidLimburg2018 = RallyItem("Rally van Zuid-Limburg 2018","Nico Degroot","Thomas Driesen",
            Date(2018-1900,11-1,11),"43"," Ambachtsweg 5  - 3890 Gingelom")
        rallyItems.add(rallyVanZuidLimburg2018)
        val rallyVanHaspengouw2020 = RallyItem("Rally van Haspengouw 2020","Nico Degroot","Veerle Bovy",
            Date(2020-1900,2-1,29),"Accident","Lichtenberglaan 1090,3800 Sint-Truiden")
        rallyItems.add(rallyVanHaspengouw2020)
        val rallyVanHaspengouw2022 = RallyItem("Rally van Haspengouw 2022","Nico Degroot","Mike van den Brink",
            Date(2022-1900,2-1,26),"Retired","Lichtenberglaan 1090,3800 Sint-Truiden")
        rallyItems.add(rallyVanHaspengouw2022)
        val rallyeSprintDeHaillot2022 = RallyItem("Rallye Sprint De Haillot","Nico Degroot","David Vanrijkelen",
            Date(2022-1900,5-1,26),"Windscreen","Rue de I'lle Dossai 12,5300 Sclayn")
        rallyItems.add(rallyeSprintDeHaillot2022)
        val rallyVanZuidLimburg2022 = RallyItem("Rally van Zuid-Limburg 2022","Nico Degroot","Thomas Driesen",
            Date(2022-1900,11-1,13),"Course Car"," Ambachtsweg 5  - 3890 Gingelom")
        rallyItems.add(rallyVanZuidLimburg2022)
        val rallyVanHaspengouw2023 = RallyItem("Rally van Haspengouw 2023","Nico Degroot","Thomas Driesen",
            Date(2023-1900,2-1,25),"Retired","Lichtenberglaan 1090,3800 Sint-Truiden")
        rallyItems.add(rallyVanHaspengouw2023)
        val sezoensrally2023 = RallyItem("Sezoensrally 2023","Nico Degroot","Thomas Driesen",
            Date(2023-1900,5-1,20),"DNS","Dorperheideweg 56, 3950 Bocholt, België")
        rallyItems.add(sezoensrally2023)


    }
}