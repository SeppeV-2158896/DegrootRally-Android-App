package be.seppevandenberk.degrootrally


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import be.seppevandenberk.degrootrally.databinding.FragmentHoofdMenuBinding

class HoofdMenuFragment : Fragment() {

    //_binding is voor lokaal gebruik, terwijl binding een get variabele is en niet vanbuiten af kan worden aangepast.
    //_binding is nullable door dat fragments aangemaakt en verwijdert moeten worden
    private var _binding: FragmentHoofdMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHoofdMenuBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}