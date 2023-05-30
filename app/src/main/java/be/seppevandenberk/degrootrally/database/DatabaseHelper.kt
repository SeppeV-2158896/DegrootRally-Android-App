package be.seppevandenberk.degrootrally.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//source: https://www.geeksforgeeks.org/android-sqlite-database-in-kotlin/

class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + EMAIL_COL + " EMAIL, " +
                NAME_COl + " NAME," +
                PASSWORD_COL + " PASSWORD" +
                TYPE_COL + " TYPE" + ")")

        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addUser(email : String, name: String, password: String, type : String) {

        val values = ContentValues()

        values.put(EMAIL_COL, email)
        values.put(NAME_COl, name)
        values.put(PASSWORD_COL, password)
        values.put(NAME_COl, name)

        // here we are creating a
        // writable variable of
        // our database as we want to
        // insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    // below method is to get
    // all data from our database
    fun getUser(): Cursor? {

        // here we are creating a readable
        // variable of our database
        // as we want to read value from it
        val db = this.readableDatabase

        // below code returns a cursor to
        // read data from the database
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

    }

    companion object {
        // here we have defined variables for our database

        private val DATABASE_NAME = "RALLY DEGROOT"

        private val DATABASE_VERSION = 1

        val TABLE_NAME = "gfg_table"
        val EMAIL_COL = "email"
        val NAME_COl = "name"
        val PASSWORD_COL = "password"
        val TYPE_COL = "password"
    }
}