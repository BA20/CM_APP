package ipvc.estg.plantme.ui.plantacoes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.plantme.R
import ipvc.estg.plantme.api.entidades.Plantacao

class PlantacoesAdaptor internal constructor(var clickListener: OnNotaClickListener) : RecyclerView.Adapter<PlantacoesAdaptor.NotaViewHolder>() {

    private var plantacoes = emptyList<Plantacao>()

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeZona: TextView = itemView.findViewById(R.id.nome_tipo)
        private val area: TextView = itemView.findViewById(R.id.data_evento)
        private val stock: TextView = itemView.findViewById(R.id.stock_adicionado)

        fun inicializar(item : Plantacao, action : OnNotaClickListener) {
            nomeZona.text = item.nome
            area.text = item.area.toString()
            stock.text = item.stock.toString()

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