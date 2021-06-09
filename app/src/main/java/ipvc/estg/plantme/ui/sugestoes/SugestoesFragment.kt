package ipvc.estg.plantme.ui.sugestoes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.plantme.R
import ipvc.estg.plantme.adapter.SugestoesAdapter

class SugestoesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //recycler view
        /*val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = SugestoesAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)*/

        val root = inflater.inflate(R.layout.fragment_sugestoes, container, false)
        return root
    }


}