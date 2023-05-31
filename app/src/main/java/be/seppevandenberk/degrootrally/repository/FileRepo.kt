package be.seppevandenberk.degrootrally.repository

import be.seppevandenberk.degrootrally.model.RallyItem

interface FileRepo {
    fun save(items : ArrayList<RallyItem>)
    fun read() : ArrayList<RallyItem>
}