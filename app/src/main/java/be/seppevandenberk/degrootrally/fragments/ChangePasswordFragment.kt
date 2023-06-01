package be.seppevandenberk.degrootrally.fragments

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import be.seppevandenberk.degrootrally.R
import be.seppevandenberk.degrootrally.database.DatabaseHelper
import be.seppevandenberk.degrootrally.database.DatabaseHelper.Companion.PASSWORD_COL
import be.seppevandenberk.degrootrally.databinding.FragmentChangePasswordBinding
import be.seppevandenberk.degrootrally.model.User
import be.seppevandenberk.degrootrally.model.ViewModelLoggedInUser

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {
    private lateinit var oldPassword: EditText
    private lateinit var newPassword: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var confirmBtn: Button
    private lateinit var binding : FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oldPassword = view.findViewById(R.id.current_password_txt_ed)
        newPassword = view.findViewById(R.id.new_password_txt_ed)
        confirmPassword = view.findViewById(R.id.confirm_password_txt_ed)
        confirmBtn = view.findViewById(R.id.change_to_new_password_btn)

        confirmBtn.setOnClickListener {
            changePassword(
                oldPassword.text.toString(),
                newPassword.text.toString(),
                confirmPassword.text.toString()
            )
            displayFragment(AccountFragment())
        }
    }
    private fun changePassword(oldPassword: String, newPassword: String, confirmPassword: String) {
        if (oldPassword == "" || newPassword == "" || confirmPassword == "") {
            Toast.makeText(requireContext(), "Fill in all the fields!", Toast.LENGTH_LONG).show()
        } else if (confirmPassword != newPassword) {
            Toast.makeText(requireContext(), "New passwords are not the same!", Toast.LENGTH_LONG)
                .show()
        } else if (oldPassword == newPassword) {
            Toast.makeText(
                requireContext(),
                "New password can't be the same as the old password!",
                Toast.LENGTH_LONG
            ).show()
        } else{
            val user = ViewModelProvider(requireActivity())[ViewModelLoggedInUser::class.java]
            user.name.observe(viewLifecycleOwner) { name ->
                val user = User(null, name, "", null, requireContext())
                val db = DatabaseHelper(requireContext(), null)

                val values = ContentValues()
                values.put(PASSWORD_COL, user.hashPassword(newPassword))

                // Specify the table name and the WHERE clause to identify the row to update
                val tableName = "gfg_table"
                val whereClause = "password = ?" // Use placeholder for the WHERE clause
                val whereArgs = arrayOf(user.hashPassword(oldPassword)) // Use actual value for the WHERE clause

                db.writableDatabase.update(tableName, values, whereClause, whereArgs)

                db.close()
                displayFragment(AccountFragment())
            }
        }

    }

    private fun displayFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragmentLayoutMain, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
