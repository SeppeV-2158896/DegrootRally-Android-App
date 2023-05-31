package be.seppevandenberk.degrootrally

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import be.seppevandenberk.degrootrally.databinding.FragmentAddRaceBinding
import be.seppevandenberk.degrootrally.databinding.FragmentKalenderEnResultatenBinding


class AddRaceFragment : Fragment() {
    private lateinit var binding: FragmentAddRaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }
}