package ipvc.estg.plantme.ui.sugestoes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import ipvc.estg.plantme.R
import ipvc.estg.plantme.api.EndPoints
import ipvc.estg.plantme.api.ServiceBuilder
import ipvc.estg.plantme.api.entidades.Produto
import ipvc.estg.plantme.api.respostas.RespostaPlantacoes
import ipvc.estg.plantme.api.respostas.RespostaProduto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SugestoesFragment : Fragment(), SugestoesAdapter.OnSugestaoClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_sugestoes, container, false)
        sharedPreferences = this.requireActivity().getSharedPreferences(getString(R.string.plantme), Context.MODE_PRIVATE)
        email = sharedPreferences.getString(getString(R.string.email_sp), "").toString()

        recyclerView = root.findViewById(R.id.recycler_view_sugestoes)
        val adapter = SugestoesAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.addItemDecoration(HorizontalDividerItemDecoration.Builder(context).build())

        val calendar = Calendar.getInstance()
        if(email != "") {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getEpocaAtual()
            call.enqueue(object : Callback<RespostaProduto> {
                override fun onResponse(call: Call<RespostaProduto>, response: Response<RespostaProduto>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            adapter.setProdutos(response.body()?.produto!!)
                        } else {
                            Toast.makeText(
                                this@SugestoesFragment.requireContext(),
                                getString(R.string.erro_api),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    else {
                        Toast.makeText(
                            this@SugestoesFragment.requireContext(),
                            getString(R.string.erro_pedido),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                override fun onFailure(call: Call<RespostaProduto>, t: Throwable) {
                    Toast.makeText(this@SugestoesFragment.requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        return root
    }

    override fun onItemClick(item: Produto, position: Int) {

    }
}