package be.seppevandenberk.degrootrally


import androidx.fragment.app.Fragment
import be.seppevandenberk.degrootrally.databinding.FragmentHoofdMenuBinding

class HoofdMenuFragment : Fragment() {

    //_binding is voor lokaal gebruik, terwijl binding een get variabele is en niet vanbuiten af kan worden aangepast.
    //_binding is nullable door dat fragments aangemaakt en verwijdert moeten worden
    private var _binding: FragmentHoofdMenuBinding? = null
    private val binding get() = _binding!!

}