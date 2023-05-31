
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.fragment.app.Fragment

import be.seppevandenberk.degrootrally.R


class AccountFragment : Fragment() {

    private lateinit var imageAccountIcon: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageAccountIcon = view.findViewById(R.id.imageAccountIcon)

        // Set initial account icon
        imageAccountIcon.setImageResource(R.drawable.ic_account)

        // Handle click on account icon
        imageAccountIcon.setOnClickListener {
            //requestPicture()
        }

    }


}
