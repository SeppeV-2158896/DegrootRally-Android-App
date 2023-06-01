package be.seppevandenberk.degrootrally.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.activities.MainActivity
import be.seppevandenberk.degrootrally.model.User
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private lateinit var registerBtn: Button
    private lateinit var passwordEditText: EditText
    private lateinit var passwordConfirmEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        registerBtn = view.findViewById(R.id.register_btn)
        passwordEditText = view.findViewById(R.id.password_new_txt_ed)
        passwordConfirmEditText = view.findViewById(R.id.password_confirm_txt_ed)
        usernameEditText = view.findViewById(R.id.username_new_txt_ed)
        emailEditText = view.findViewById(R.id.email_new_txt_ed)

        registerBtn.setOnClickListener {
            register()
        }

        return view
    }

    private fun register() {
        if (passwordEditText.text.isEmpty() || passwordConfirmEditText.text.isEmpty() || usernameEditText.text.isEmpty() || emailEditText.text.isEmpty()) {
            view?.let{rootView -> Snackbar.make(rootView, "Need to fill in all the fields", Snackbar.LENGTH_SHORT).show()}
            return
        }
        if (passwordEditText.text.toString().length < 5) {
            view?.let{rootView -> Snackbar.make(rootView, "Password is too short, choose a longer password", Snackbar.LENGTH_SHORT).show()}
            return
        }
        if (passwordEditText.text.toString() == "Password") {
            view?.let{rootView -> Snackbar.make(rootView, "Password is too simple, choose a better password", Snackbar.LENGTH_SHORT).show()}
            return
        }
        if (usernameEditText.text.toString() == "Account") {
            view?.let{rootView -> Snackbar.make(rootView, "Username is invalid, choose an other username", Snackbar.LENGTH_SHORT).show()}
            return
        }
        if (passwordEditText.text.toString() != passwordConfirmEditText.text.toString()) {
            view?.let{rootView -> Snackbar.make(rootView, "Password are not the same", Snackbar.LENGTH_SHORT).show()}
            return
        }

        val user = User(
            emailEditText.text.toString(),
            usernameEditText.text.toString(),
            passwordEditText.text.toString(),
            "User",
            requireContext()
        )

        if (user.checkForAccountWithSameEmail()) {
            view?.let{rootView -> Snackbar.make(rootView, "Account with same email already exists", Snackbar.LENGTH_SHORT).show()}
            return
        }

        val cursor = user.checkForAccountWithSameUsername()
        if (cursor != null) {
            cursor.close()
            view?.let{rootView -> Snackbar.make(rootView, "Account with same username already exists", Snackbar.LENGTH_SHORT).show()}
            return
        }

        user.hashPassword(passwordEditText.text.toString())
        user.create()

        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("name", user.username)
        startActivity(intent)
    }
}