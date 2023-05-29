package be.seppevandenberk.degrootrally.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import be.seppevandenberk.degrootrally.LoginActivity
import be.seppevandenberk.degrootrally.MainActivity
import be.seppevandenberk.degrootrally.R
import com.google.android.material.textfield.TextInputEditText


class LoginFragment : Fragment(R.layout.fragment_login){

    private lateinit var loginBtn : Button
    private lateinit var guestTxt : TextView
    private lateinit var registerTxt : TextView
    private lateinit var passwordForgotTxt : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container,false)

        loginBtn = view.findViewById(R.id.login_btn)
        guestTxt = view.findViewById(R.id.guest_txt_vw)
        registerTxt = view.findViewById(R.id.register_txt_vw)
        passwordForgotTxt = view.findViewById(R.id.forgot_txt_vw)

        loginBtn.setOnClickListener{
            val intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
            Log.i("pressed","pressed button")
            //TODO check credentials
        }
        guestTxt.setOnClickListener{
            //TODO: login guest
            Log.i("pressed","pressed guest")
        }
        registerTxt.setOnClickListener{
            //TODO: open register fragment
            Log.i("pressed","pressed register")
        }
        passwordForgotTxt.setOnClickListener{
            //TODO: open forgot password fragment
            Log.i("pressed","pressed password")
        }

        return view
    }

}