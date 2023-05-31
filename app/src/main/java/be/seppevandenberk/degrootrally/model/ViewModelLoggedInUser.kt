package be.seppevandenberk.degrootrally.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelLoggedInUser: ViewModel() {
    val name = MutableLiveData<String>()
}