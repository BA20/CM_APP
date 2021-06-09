package ipvc.estg.plantme.ui.sugestoes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.plantme.R
import ipvc.estg.plantme.api.entidades.Produto
import java.text.SimpleDateFormat

class SugestoesAdapter internal constructor(var clickListener: OnSugestaoClickListener) : RecyclerView.Adapter<SugestoesAdapter.SugestoesViewHolder>() {

    private var sugestao = emptyList<Produto>()

    class SugestoesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomePlanta: TextView = itemView.findViewById(R.id.nomePlanta)
        val dataInicioV: TextView = itemView.findViewById(R.id.dataInicio)
        val dataFimV: TextView = itemView.findViewById(R.id.dataFim)

        fun inicializar(item : Produto, action : OnSugestaoClickListener) {
            val pattern = "dd-MM"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val dataInicio = simpleDateFormat.format(item.dataInicio)
            val dataFim = simpleDateFormat.format(item.dataFim)
            nomePlanta.text = item.nomePlanta
            dataInicioV.text = dataInicio.toString()
            dataFimV.text = dataFim.toString()

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SugestoesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.sugestoes_line, parent, false)
        return SugestoesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SugestoesViewHolder, position: Int) {
        val current = sugestao[position]

        holder.inicializar(current, clickListener)
    }

    override fun getItemCount(): Int {
        return sugestao.size
    }

    internal fun setProdutos(produto: List<Produto>) {
        this.sugestao = produto
        notifyDataSetChanged()
    }

    interface OnSugestaoClickListener {
        fun onItemClick(item : Produto, position: Int)
    }
}