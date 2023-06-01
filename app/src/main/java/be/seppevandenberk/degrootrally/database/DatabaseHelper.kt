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
                NAME_COL + " NAME," +
                PASSWORD_COL + " PASSWORD," +
                TYPE_COL + " TYPE" + ")")

        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    //TODO test
    fun addUser(email: String, name: String, password: String, type: String) {
        val values = ContentValues()

        values.put(EMAIL_COL, email)
        values.put(NAME_COL, name)
        values.put(PASSWORD_COL, password)
        values.put(TYPE_COL, type)

        // here we are creating a writable variable of
        // our database as we want to insert value in our database
        val db = this.writableDatabase

        // all values are inserted into database
        db.insert(TABLE_NAME, null, values)

        db.close()
    }

    // below method is to get all data from our database
    //TODO test
    fun getUser(): Cursor? {
        // here we are creating a readable variable of our database as we want to read value from it
        val db = this.readableDatabase

        // below code returns a cursor to read data from the database
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            return cursor
        }

        return null
    }

    companion object {
        // here we have defined variables for our database
        private const val DATABASE_NAME = "RALLY DEGROOT"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "gfg_table"
        const val EMAIL_COL = "email"
        const val NAME_COL = "name"
        const val PASSWORD_COL = "password"
        const val TYPE_COL = "type"
    }

    fun getPath(): String {
        return writableDatabase.path
    }
}