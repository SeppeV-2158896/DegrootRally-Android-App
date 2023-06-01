package be.seppevandenberk.degrootrally.model

import java.math.BigDecimal
import java.util.Date

data class RallyItem(
    val title: String,
    val piloot: String,
    val copiloot: String,
    val date: Date,
    var result: String
) {

}
