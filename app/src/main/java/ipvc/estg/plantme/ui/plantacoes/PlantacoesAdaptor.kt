package ipvc.estg.plantme.ui.plantacoes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.plantme.R

class PlantacoesAdaptor internal constructor(var clickListener: OnNotaClickListener) : RecyclerView.Adapter<PlantacoesAdaptor.NotaViewHolder>() {

    private var plantacoes = emptyList<Plantacao>()

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeZona: TextView = itemView.findViewById(R.id.zona)
        private val nomePlanta: TextView = itemView.findViewById(R.id.planta)

        fun inicializar(item : Plantacao, action : OnNotaClickListener) {
            nomeZona.text = item.zona
            nomePlanta.text = item.planta

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.platacoes_line, parent, false)
        return NotaViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return plantacoes.size
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = plantacoes[position]

        holder.inicializar(current, clickListener)
    }

    internal fun setPlantacoes(plantacoes: List<Plantacao>) {
        this.plantacoes = plantacoes
        notifyDataSetChanged()
    }

    interface OnNotaClickListener {
        fun onItemClick(item : Plantacao, position: Int)
    }
}