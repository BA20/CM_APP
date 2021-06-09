package ipvc.estg.plantme.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.plantme.R
import ipvc.estg.plantme.entity.Planta

class SugestoesAdapter internal constructor(context: Context) : RecyclerView.Adapter<SugestoesAdapter.SugestoesViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var planta = emptyList<Planta>()

    class SugestoesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomePlanta: TextView = itemView.findViewById(R.id.textView5)
        val especiePlanta: TextView = itemView.findViewById(R.id.textView6)
        val temperatura: TextView = itemView.findViewById(R.id.textView7)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugestoesViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return SugestoesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SugestoesViewHolder, position: Int) {
        val current = planta[position]

        holder.nomePlanta.text = current.nomePlanta
        holder.especiePlanta.text = current.especie
        holder.temperatura.text = current.temperatura.toString()
    }

    override fun getItemCount() = planta.size
}