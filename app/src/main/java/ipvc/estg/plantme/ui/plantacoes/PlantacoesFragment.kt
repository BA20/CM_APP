package ipvc.estg.plantme.ui.plantacoes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import ipvc.estg.plantme.R


class PlantacoesFragment : Fragment(), PlantacoesAdaptor.OnNotaClickListener {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_plantacoes, container, false)

        recyclerView = root.findViewById(R.id.recyclerview_plantacoes)
        val adaptor = PlantacoesAdaptor(this)
        recyclerView.adapter = adaptor
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.addItemDecoration(HorizontalDividerItemDecoration.Builder(context).build())

        val plantacoesList = mutableListOf<Plantacao>()
        val pl1 = Plantacao("Zona 1", "Milho")
        val pl2 = Plantacao("Zona 2", "Maceiras")
        val pl3 = Plantacao("Zona 3", "Bananeira")
        plantacoesList.add(pl1)
        plantacoesList.add(pl2)
        plantacoesList.add(pl3)

        adaptor.setPlantacoes(plantacoesList)

        return root
    }

    override fun onItemClick(item: Plantacao, position: Int) {

    }
}