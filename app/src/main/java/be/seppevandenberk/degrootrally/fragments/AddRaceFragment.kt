package be.seppevandenberk.degrootrally.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.FragmentAddRaceBinding
import be.seppevandenberk.degrootrally.model.RallyAdapter
import be.seppevandenberk.degrootrally.model.RallyItem
import be.seppevandenberk.degrootrally.repository.RallyItemsFileRepo
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddRaceFragment : Fragment(R.layout.fragment_add_race) {
    private val rallyItems = mutableListOf<RallyItem>()
    private lateinit var binding: FragmentAddRaceBinding
    private lateinit var date: Date

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRaceBinding.inflate(layoutInflater)

        date = Calendar.getInstance().time

        val rallyItemsFileRepo = this.context?.let { it1 -> RallyItemsFileRepo(it1) }
        if (rallyItemsFileRepo != null && rallyItemsFileRepo.read().size != rallyItems.size) {
            rallyItems.addAll(rallyItemsFileRepo.read())
        }

        sortRallyItemsByDate(rallyItems as ArrayList<RallyItem>)

        val adapter = RallyAdapter(rallyItems)

        putArgsInFields()

        binding.addRaceBtn.setOnClickListener {
            val newRallyItem = RallyItem(
                binding.titleTxtEd.text.toString(),
                binding.pilotTxtEd.text.toString(),
                binding.copilotTxtEd.text.toString(),
                date,
                binding.resultTxtEd.text.toString(),
                binding.addressTxtEd.text.toString()
            )
            arguments?.getInt("position")?.let { it1 -> rallyItems.removeAt(it1) }
            rallyItems.add(newRallyItem)
            adapter.notifyItemInserted(rallyItems.size - 1)
            if (rallyItemsFileRepo != null) {
                rallyItemsFileRepo.delete()
                rallyItemsFileRepo.save(rallyItems)
            }
            displayFragment(KalenderEnResultatenFragment())
        }

        binding.dateTxtVw.setOnClickListener {
            showDatePicker()
        }

        return binding.root
    }

    private fun displayFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragmentLayoutMain, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val formattedDate =
                    String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.dateTxtVw.text = "Date: $formattedDate"

                date = selectedDate.time
            }, year, month, day)

        datePicker.show()
    }

    private fun putArgsInFields() {
        binding.titleTxtEd.setText(arguments?.getString("title") ?: "")

        if (arguments?.getLong("date") != null) {
            date = arguments?.getLong("date")?.let { Date(it) }!!
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(date)

            binding.dateTxtVw.text = "Date: $formattedDate"

            //The date will always be set if the edit button is pressed because if the user
            // doesn't select a date, the app will put the current date as the date of the event.
            binding.addRaceBtn.text = "Save"
        }

        binding.pilotTxtEd.setText(arguments?.getString("pilot") ?: "")
        binding.copilotTxtEd.setText(arguments?.getString("copilot") ?: "")
        binding.resultTxtEd.setText(arguments?.getString("result") ?: "")
        binding.addressTxtEd.setText(arguments?.getString("address") ?: "")
    }
    //TODO test
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
}