package be.seppevandenberk.degrootrally.fragments

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.activities.MainActivity
import be.seppevandenberk.degrootrally.database.DatabaseHelper
import be.seppevandenberk.degrootrally.model.User
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var loginBtn: Button
    private lateinit var guestTxt: TextView
    private lateinit var registerTxt: TextView
    private lateinit var passwordForgotTxt: TextView
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var adminTxt: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        loginBtn = view.findViewById(R.id.login_btn)
        guestTxt = view.findViewById(R.id.guest_txt_vw)
        adminTxt = view.findViewById(R.id.admin_txt_vw)
        registerTxt = view.findViewById(R.id.register_txt_vw)
        passwordForgotTxt = view.findViewById(R.id.forgot_txt_vw)
        passwordEditText = view.findViewById(R.id.password_txt_ed)
        usernameEditText = view.findViewById(R.id.login_txt_ed)


        loginBtn.setOnClickListener {
            val password = passwordEditText.text.toString()
            val username = usernameEditText.text.toString()
            if (password == "Password" || username == "Account") {
                view?.let { rootView ->
                    Snackbar.make(
                        rootView, "Need to fill in all the fields", Snackbar.LENGTH_SHORT
                    ).show()
                }
            } else {
                val user = User(null, username, password, "User", requireContext())
                if (user.checkAccessGranted(username, password)) {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.putExtra("name",username)
                    startActivity(intent)
                    Log.i("pressed", "pressed button")
                }
            }
        }
        guestTxt.setOnClickListener {
            val user = getGuestAccount()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("name",user)
            startActivity(intent)

            Log.i("pressed", "pressed guest")
        }
        adminTxt.setOnClickListener {
            val user = getAdminAccount()
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("name",user)
            startActivity(intent)

            Log.i("pressed", "pressed admin")
        }
        registerTxt.setOnClickListener {
            displayFragment(SignUpFragment())
            Log.i("pressed", "pressed register")
        }
        passwordForgotTxt.setOnClickListener {
            displayFragment(ForgotPasswordFragment())
            Log.i("pressed", "pressed password")
        }

        return view
    }

    fun displayFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragmentLayoutLogin, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun getGuestAccount(): String {

        val id =
            Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        val db = context?.let { DatabaseHelper(it, null) }
        var userExists = false
        if (db != null) {
            val cursor = db.getUser()
            if (cursor != null) {
                val index = cursor.getColumnIndex(DatabaseHelper.NAME_COL)
                if (index >= 0) {
                    if (cursor.getString(index) != null) {

                        if (cursor.getString(index).equals(id)) {
                            userExists = true
                        }
                        while (cursor.moveToNext()) {
                            if (cursor.getString(index).equals(id)) {
                                userExists = true
                                break;
                            }
                        }
                    }
                }
                cursor.close()
            }
            db.close()
        }

        if(!userExists) {
            val user = User(null, id, "guest", "Guest", requireContext())
            user.create()
        }
        return id
    }

    fun getAdminAccount(): String {

        val db = context?.let { DatabaseHelper(it, null) }
        var adminExists = false
        if (db != null) {
            val cursor = db.getUser()
            if(cursor != null) {
                val index = cursor.getColumnIndex(DatabaseHelper.NAME_COL)

                if (index >= 0){
                    if (cursor.getString(index) != null) {
                        if (cursor.getString(index).equals("Admin")) {
                            adminExists = true
                        }
                        while (cursor.moveToNext()) {
                            if (cursor.getString(index).equals("Admin")) {
                                adminExists = true
                            }
                        }
                    }
                }
                cursor.close()
            }
            db.close()

        }
        if(!adminExists) {
            val user = User(null, "Admin", "admin", "Admin", requireContext())
            user.create()
        }
        return "Admin"
    }


}