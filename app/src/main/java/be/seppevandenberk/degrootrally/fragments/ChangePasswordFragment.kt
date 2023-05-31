import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import be.seppevandenberk.degrootrally.R

class ChangePasswordFragment : Fragment() {

    private lateinit var oldPassword : EditText
    private lateinit var newPassword : EditText
    private lateinit var confirmPassword : EditText
    private lateinit var confirmBtn : Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_change_password, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oldPassword = view.findViewById(R.id.current_password_txt_ed)
        newPassword = view.findViewById(R.id.new_password_txt_ed)
        confirmPassword = view.findViewById(R.id.confirm_password_txt_ed)
        confirmBtn = view.findViewById(R.id.change_to_new_password_btn)

        confirmBtn.setOnClickListener{
            changePassword(oldPassword.text.toString(),newPassword.text.toString(),confirmPassword.text.toString())
        }

    }

    private fun changePassword(oldPassword : String,newPassword : String, confirmPassword: String){
        if(oldPassword == "" || newPassword == "" ||confirmPassword == "" ){
            Toast.makeText(requireContext(), "Fill in all the fields!", Toast.LENGTH_LONG).show()
        }
        else if (confirmPassword != newPassword){
            Toast.makeText(requireContext(), "New passwords are not the same!", Toast.LENGTH_LONG).show()
        }
        else if (oldPassword == newPassword){
            Toast.makeText(requireContext(), "New password can't be the same as the old password!", Toast.LENGTH_LONG).show()
        }

    }

}
