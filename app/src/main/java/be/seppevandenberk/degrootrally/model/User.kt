package be.seppevandenberk.degrootrally.model

import android.content.Context
import android.database.Cursor
import android.util.Log
import android.widget.Toast
import be.seppevandenberk.degrootrally.database.DatabaseHelper
import java.security.MessageDigest


data class User(
    private var email: String?,
    val username: String,
    val password: String,
    private var type: String?,
    val context: Context
) {

    fun create() {
        if (email == null) {
            this.email = "NaN"
        }
        if (type == null) {
            this.type = "User"
        }
        val db = DatabaseHelper(context, null)
        db.addUser(email!!, username, hashPassword(password), type!!)
        Toast.makeText(context, "Account has been registered", Toast.LENGTH_LONG).show()
    }


    fun checkForAccountWithSameEmail(email: String): Boolean {
        val db = context?.let { DatabaseHelper(it, null) }
        if (db != null) {
            Log.i("Path", db.getPath())
            val cursor = db.getUser() ?: return false
            val index = cursor.getColumnIndex(DatabaseHelper.EMAIL_COL)

            if (index >= 0) {
                if (cursor.getString(index) == null) {
                    return false
                }
                if (cursor.getString(index).equals(email)) {
                    db.close()
                    cursor.close()
                    return true
                }
                while (cursor.moveToNext()) {
                    if (cursor.getString(index).equals(email)) {
                        db.close()
                        cursor.close()
                        return true
                    }
                }
                db.close()
                cursor.close()
            }

        }
        return false
    }

    fun checkForAccountWithSameUsername(username: String): Cursor? {
        val db = context?.let { DatabaseHelper(it, null) }
        if (db != null) {
            val cursor = db.getUser() ?: return null
            val index = cursor.getColumnIndex(DatabaseHelper.NAME_COL)
            if (index >= 0) {
                if (cursor.getString(index).equals(username)) {
                    db.close()
                    return cursor
                }
                while (cursor.moveToNext()) {
                    if (cursor.getString(index).equals(username)) {
                        db.close()
                        return cursor
                    }
                }
                db.close()
                cursor.close()
            }

        }
        return null
    }

    fun hashPassword(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashBytes = messageDigest.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun compareHashedPassword(input: String, hashedPassword: String): Boolean {
        val hashedInput = hashPassword(input)
        return hashedInput == hashedPassword
    }

    fun checkAccessGranted(username: String, password: String): Boolean {
        val db = context?.let { DatabaseHelper(it, null) }
        if (db != null) {
            val cursor = checkForAccountWithSameUsername(username) ?: return false
            val index = cursor.getColumnIndex(DatabaseHelper.PASSWORD_COL)
            if (index >= 0) {
                if (compareHashedPassword(password, cursor.getString(index))) {
                    db.close()
                    cursor.close()
                    return true
                }
                db.close()
                cursor.close()
            }

        }
        return false
    }

    fun getType(): String {
        val db = context.let { DatabaseHelper(it, null) }
        if(db != null) {
            val cursor = checkForAccountWithSameUsername(username) ?: return ""
            val index = cursor.getColumnIndex(DatabaseHelper.TYPE_COL)
            val type = cursor.getString(index)
            db.close()
            cursor.close()
            return type
        }
        return ""
    }

    fun getEmail(): String {
        val db = context.let { DatabaseHelper(it, null) }
        if(db != null) {
            val cursor = checkForAccountWithSameUsername(username) ?: return ""
            val index = cursor.getColumnIndex(DatabaseHelper.EMAIL_COL)
            val email = cursor.getString(index)
            db.close()
            cursor.close()
            return email
        }
        return ""
    }



}