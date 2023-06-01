package be.seppevandenberk.degrootrally.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.databinding.FragmentEditNewsBinding
import be.seppevandenberk.degrootrally.repository.NewsFileRepo
import be.seppevandenberk.degrootrally.repository.RallyItemsFileRepo

class EditNewsFragment : Fragment() {
    private lateinit var binding: FragmentEditNewsBinding
    private lateinit var oldNews: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditNewsBinding.inflate(layoutInflater)

        val newsFileRepo = this.context?.let { it1 -> NewsFileRepo(it1) }

        if (newsFileRepo != null) {
            oldNews = newsFileRepo.readString().toString()
        }

        binding.txtNewsEd.setText(oldNews)

        binding.delNewsBtn.setOnClickListener {
            binding.txtNewsEd.setText("")
        }

        binding.saveNewsBtn.setOnClickListener {
            if (newsFileRepo != null) {
                newsFileRepo.delete()
                newsFileRepo.saveString(binding.txtNewsEd.text.toString())
            }
            displayFragment(HoofdMenuFragment())
        }

        return binding.root
    }

    private fun displayFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragmentLayoutMain, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}