package be.seppevandenberk.degrootrally

import be.seppevandenberk.degrootrally.fragments.HoofdMenuFragment
import be.seppevandenberk.degrootrally.model.RallyItem
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.Date

class DegrootRallyUnitTest {
    @Test
    fun sortRallyItemsByDate_isCorrect(){
        val date1 = Date(2023-1900, 9, 16)
        val date2 = Date(2023-1900, 4, 25)
        val date3 = Date(2023-1900, 5, 1)

        val item1 = RallyItem("Race1", "Jan-Mathijs", "Seppe", date1, "1", "Rallystraat")
        val item2 = RallyItem("Race2", "Pex", "Vandenberk", date2, "1", "Degrootelaan")
        val item3 = RallyItem("Race3", "Kris", "Wouter", date3, "1", "Modderweg")

        val rallyItems = mutableListOf<RallyItem>()
        rallyItems.add(item1)
        rallyItems.add(item2)
        rallyItems.add(item3)

        val expectedSortedRallyItems = mutableListOf<RallyItem>()
        expectedSortedRallyItems.add(item2)
        expectedSortedRallyItems.add(item3)
        expectedSortedRallyItems.add(item1)

        val fragment = HoofdMenuFragment()
        val sortedRallyItems = fragment.sortRallyItemsByDate(rallyItems as ArrayList<RallyItem>)

        assertEquals(expectedSortedRallyItems, sortedRallyItems)
    }
}