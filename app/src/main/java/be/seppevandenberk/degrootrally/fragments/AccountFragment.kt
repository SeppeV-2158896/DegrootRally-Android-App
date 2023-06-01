package be.seppevandenberk.degrootrally.fragments
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider

import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.model.User
import be.seppevandenberk.degrootrally.model.ViewModelLoggedInUser

class AccountFragment : Fragment() {
    private lateinit var imageAccountIcon: ImageView
    private lateinit var changePasswordButton: Button
    private lateinit var loginButton: Button
    private lateinit var logoutButton: Button
    private lateinit var userName : TextView
    private lateinit var email : TextView
    private var imageUri : Uri? = null
    private val changePasswordFragment = ChangePasswordFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageAccountIcon = view.findViewById(R.id.account_image_ic)
        changePasswordButton = view.findViewById(R.id.change_password_btn)
        loginButton = view.findViewById(R.id.login_guest_btn)
        logoutButton = view.findViewById(R.id.logout_btn)
        userName = view.findViewById(R.id.username_txt_vw)
        email = view.findViewById(R.id.email_txt_vw)

        imageAccountIcon.setImageResource(R.drawable.ic_account)

        val user = ViewModelProvider(requireActivity())[ViewModelLoggedInUser::class.java]
        user.name.observe(viewLifecycleOwner){name ->
            val user = User(null,name,"",null,requireContext())
            val type = user.getType()
            if (type == "Guest"){
                userName.text = "Guest"
                email.visibility = View.INVISIBLE
                changePasswordButton.visibility = View.INVISIBLE
                loginButton.visibility = View.VISIBLE
                logoutButton.visibility = View.INVISIBLE

            }
            else if (type == "Admin"){
                userName.text = "Admin"
                email.visibility = View.INVISIBLE
                changePasswordButton.visibility = View.INVISIBLE
                loginButton.visibility = View.INVISIBLE
                logoutButton.visibility = View.VISIBLE
            }
            else {
                email.visibility = View.VISIBLE
                userName.text = name
                val data = user.getEmail()
                email.text = data
                changePasswordButton.visibility = View.VISIBLE
                loginButton.visibility = View.INVISIBLE
                logoutButton.visibility = View.VISIBLE
            }
        }

        // Handle click on account icon
        imageAccountIcon.setOnClickListener {
            requestPicture()
        }

        loginButton.setOnClickListener{
            val intent = requireActivity().baseContext.packageManager.getLaunchIntentForPackage(requireActivity().baseContext.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            requireActivity().finish()
            startActivity(intent)
        }

        logoutButton.setOnClickListener{
            val loggedInUser = ViewModelProvider(this)[ViewModelLoggedInUser::class.java]
            loggedInUser.name.value = ""
            val intent = requireActivity().baseContext.packageManager.getLaunchIntentForPackage(requireActivity().baseContext.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            requireActivity().finish()
            startActivity(intent)
        }

        changePasswordButton.setOnClickListener{
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentLayoutLogin, changePasswordFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun requestPicture(){
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            imageAccountIcon.setImageURI(imageUri)
        }
    }
}
