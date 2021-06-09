package ipvc.estg.plantme.ui.plantacoes

import android.content.Context
import android.content.Intent
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
import ipvc.estg.plantme.Home
import ipvc.estg.plantme.R
import ipvc.estg.plantme.api.EndPoints
import ipvc.estg.plantme.api.ServiceBuilder
import ipvc.estg.plantme.api.entidades.Plantacao
import ipvc.estg.plantme.api.respostas.RespostaPlantacoes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlantacoesFragment : Fragment(), PlantacoesAdaptor.OnPlantacaoClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var email: String

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_plantacoes, container, false)
        sharedPreferences = this.requireActivity().getSharedPreferences(getString(R.string.plantme), Context.MODE_PRIVATE)
        email = sharedPreferences.getString(getString(R.string.email_sp), "").toString()

        recyclerView = root.findViewById(R.id.recyclerview_plantacoes)
        val adaptor = PlantacoesAdaptor(this)
        recyclerView.adapter = adaptor
        recyclerView.layoutManager = LinearLayoutManager(root.context)
        recyclerView.addItemDecoration(HorizontalDividerItemDecoration.Builder(context).build())

        if(email != "") {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getAllPlantacoes(email = email)
            call.enqueue(object : Callback<RespostaPlantacoes> {
                override fun onResponse(call: Call<RespostaPlantacoes>, response: Response<RespostaPlantacoes>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            adaptor.setPlantacoes(response.body()?.plantacoes!!)
                        } else {
                            Toast.makeText(
                                this@PlantacoesFragment.requireContext(),
                                getString(R.string.erro_api),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    else {
                        Toast.makeText(
                            this@PlantacoesFragment.requireContext(),
                            getString(R.string.erro_pedido),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                override fun onFailure(call: Call<RespostaPlantacoes>, t: Throwable) {
                    Toast.makeText(this@PlantacoesFragment.requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        return root
    }

    override fun onItemClick(item: Plantacao, position: Int) {
        val intent = Intent(this.requireContext(), DetalhesPlantacao::class.java)
        intent.putExtra("plantacao", item)
        startActivity(intent)
    }
}