package be.seppevandenberk.degrootrally.repository

import android.content.Context
import android.util.Log
import be.seppevandenberk.degrootrally.model.RallyItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileReader

class RallyItemsFileRepo(val context : Context) : FileRepo{
    override fun save(items: ArrayList<RallyItem>) {
        val fileName = "rallyItemsFile.txt"
        val fos:FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(items)
        fos.write(json.toByteArray())
        fos.close()
    }

    override fun read(): ArrayList<RallyItem> {
        return try{
            val where: File = context.filesDir
            val fileName = where.absolutePath + "/rallyItemsFile.txt"

            val gson = Gson()
            val rallyItemsListType = object : TypeToken<ArrayList<RallyItem>>() {}.type
            val model = gson.fromJson<ArrayList<RallyItem>>(FileReader(fileName), rallyItemsListType)
            model
        } catch (e: FileNotFoundException){
            ArrayList(0)
        }
    }

    override fun delete(){
        //val where: File = context.filesDir
        val filePath = context.filesDir.absolutePath + "/rallyItemsFile.txt"
        val file = File(filePath)
        if(file.exists()){
            file.delete()
            Log.i("File", "file deleted")
        }
    }
}