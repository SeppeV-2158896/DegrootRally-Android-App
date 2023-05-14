package be.seppevandenberk.degrootrally

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import be.seppevandenberk.degrootrally.databinding.ActivityMainBinding
import java.time.LocalDate
import java.util.*

class KalenderFragment : Fragment(R.layout.fragment_kalender) {

    override fun onCreate(savedInstanceState: Bundle?) {
        val rally1 = RallyItem("test","Nico", "Thomas", Date(2023,2,15),null)
        var sampleItems = listOf(rally1)

        var adapter = RallyAdapter(sampleItems)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
    }
}