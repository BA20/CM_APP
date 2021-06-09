package ipvc.estg.plantme.ui.plantacoes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.plantme.R
import ipvc.estg.plantme.api.entidades.Plantacao

class PlantacoesAdaptor internal constructor(var clickListener: OnPlantacaoClickListener) : RecyclerView.Adapter<PlantacoesAdaptor.PlantacoesViewHolder>() {

    private var plantacoes = emptyList<Plantacao>()

    class PlantacoesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomeZona: TextView = itemView.findViewById(R.id.nome_tipo)
        private val area: TextView = itemView.findViewById(R.id.area_plantacao)
        private val stock: TextView = itemView.findViewById(R.id.nomeProduto)

        fun inicializar(item : Plantacao, action : OnPlantacaoClickListener) {
            nomeZona.text = item.nome
            area.text = item.area.toString()
            stock.text = item.nomePlanta

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantacoesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.platacoes_line, parent, false)
        return PlantacoesViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return plantacoes.size
    }

    override fun onBindViewHolder(holder: PlantacoesViewHolder, position: Int) {
        val current = plantacoes[position]

        holder.inicializar(current, clickListener)
    }

    internal fun setPlantacoes(plantacoes: List<Plantacao>) {
        this.plantacoes = plantacoes
        notifyDataSetChanged()
    }

    interface OnPlantacaoClickListener {
        fun onItemClick(item : Plantacao, position: Int)
    }
}