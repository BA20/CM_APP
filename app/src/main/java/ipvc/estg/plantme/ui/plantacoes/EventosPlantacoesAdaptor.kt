package ipvc.estg.plantme.ui.plantacoes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.plantme.R
import ipvc.estg.plantme.api.entidades.Evento
import java.text.SimpleDateFormat

class EventosPlantacoesAdaptor internal constructor(var clickListener: OnNotaClickListener) : RecyclerView.Adapter<EventosPlantacoesAdaptor.NotaViewHolder>() {

    private var eventosPlantacoes = emptyList<Evento>()

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nome: TextView = itemView.findViewById(R.id.nome_tipo)
        private val data: TextView = itemView.findViewById(R.id.data_evento)
        private val stock: TextView = itemView.findViewById(R.id.stock_adicionado)

        @SuppressLint("SetTextI18n", "SimpleDateFormat")
        fun inicializar(item: Evento, action: OnNotaClickListener) {
            when (item.tipo) {
                0 -> {
                    nome.text = "Colheita"
                }
                1 -> {
                    nome.text = "Irrigação"
                }
                else -> {
                    nome.text = "Plantação"
                }
            }

            val pattern = "dd-MM-yyyy HH:mm"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val dataSTR = simpleDateFormat.format(item.data)
            data.text = dataSTR.toString()
            stock.text = item.stock_adicionado.toString()

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.eventos_plantacao_line, parent, false)
        return NotaViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventosPlantacoes.size
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = eventosPlantacoes[position]

        holder.inicializar(current, clickListener)
    }

    internal fun setPlantacoes(eventos: List<Evento>) {
        this.eventosPlantacoes = eventos
        notifyDataSetChanged()
    }

    interface OnNotaClickListener {
        fun onItemClick(item: Evento, position: Int)
    }
}