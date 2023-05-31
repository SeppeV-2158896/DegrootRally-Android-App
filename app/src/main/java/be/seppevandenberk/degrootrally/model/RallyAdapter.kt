package be.seppevandenberk.degrootrally.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.seppevandenberk.degrootrally.R
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class RallyAdapter(val items: List<RallyItem>) :
    RecyclerView.Adapter<RallyAdapter.RallyItemViewHolder>() {
    inner class RallyItemViewHolder(currentItemView: View) :
        RecyclerView.ViewHolder(currentItemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RallyItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rally_item_view, parent, false)
        return RallyItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RallyItemViewHolder, position: Int) {
        val currentRally = items[position]
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentRally.date)

        holder.itemView.apply {
            findViewById<TextView>(R.id.TitelRallyEnDatum).text =
                currentRally.title.plus(" - ").plus(formattedDate)
            if (currentRally.result != BigDecimal.ZERO) {
                findViewById<TextView>(R.id.Resultaat).text = currentRally.result.toString()
            } else {
                findViewById<TextView>(R.id.Resultaat).text = " "
            }
            findViewById<TextView>(R.id.PilootEnCoPiloot).text =
                currentRally.piloot.plus(" - ").plus(currentRally.copiloot)

        }
    }

    override fun getItemCount(): Int = items.size

}