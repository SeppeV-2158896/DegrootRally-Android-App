package be.seppevandenberk.degrootrally.repository

import android.content.Context
import be.seppevandenberk.degrootrally.model.RallyItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

class NewsFileRepo(val context: Context) : FileRepo{
    override fun save(items: ArrayList<RallyItem>) {
        val fileName = "newsFile.txt"
        val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(items)
        fos.write(json.toByteArray())
        fos.close()
    }

    override fun read(): ArrayList<RallyItem> {
        return try{
            val where: File = context.filesDir
            val fileName = where.absolutePath + "/newsFile.txt"

            val gson = Gson()
            val newsType = object : TypeToken<ArrayList<RallyItem>>() {}.type
            val model = gson.fromJson<ArrayList<RallyItem>>(FileReader(fileName), newsType)
            model
        } catch (e: FileNotFoundException){
            ArrayList(0)
        }
    }

    override fun delete() {
        val filePath = context.filesDir.absolutePath + "/newsFile.txt"
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
    }

    override fun saveString(string: String) {
        val fileName = "newsFile.txt"
        val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fos.write(string.toByteArray())
        fos.close()
    }

    override fun readString(): String? {
        return try {
            val fileName = "newsFile.txt"
            val file = File(context.filesDir, fileName)
            val inputStream = FileInputStream(file)
            val data = inputStream.bufferedReader().use { it.readText() }
            inputStream.close()
            data
        } catch (e: FileNotFoundException) {
            null
        }
    }
}