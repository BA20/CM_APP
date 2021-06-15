package ipvc.estg.plantme.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import ipvc.estg.plantme.R
import ipvc.estg.plantme.api.EndPoints
import ipvc.estg.plantme.api.ServiceBuilder
import ipvc.estg.plantme.api.entidades.Plantacao
import ipvc.estg.plantme.api.respostas.RespostaPlantacoes
import ipvc.estg.plantme.api.respostas.RespostaVenda
import ipvc.estg.plantme.ui.plantacoes.DetalhesPlantacao
import ipvc.estg.plantme.ui.plantacoes.PlantacoesAdaptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeFragment : Fragment(), PlantacoesAdaptor.OnPlantacaoClickListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        sharedPreferences = this.requireActivity().getSharedPreferences(getString(R.string.plantme), Context.MODE_PRIVATE)
        email = sharedPreferences.getString(getString(R.string.email_sp), "").toString()
        val plantacao1 = root.findViewById<TextView>(R.id.plantacao1)
        val plantacao2 = root.findViewById<TextView>(R.id.plantacao2)
        val imagePlantacao1 = root.findViewById<ImageView>(R.id.imagePlantacao1)
        val imagePlantacao2 = root.findViewById<ImageView>(R.id.imagePlantacao2)

        imagePlantacao1.setOnClickListener {
            val intent = Intent(this, DetalhesPlantacao::class.java)
            startActivity(intent)
        }
        
        imagePlantacao2.setOnClickListener {
            val intent = Intent(this, DetalhesPlantacao::class.java)
            startActivity(intent)
        }

        val c = Calendar.getInstance()
        val month = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)

        if(email != "") {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getVendasMesAno(ano = year, mes = month)
            call.enqueue(object : Callback<RespostaVenda> {
                override fun onResponse(call: Call<RespostaVenda>, response: Response<RespostaVenda>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            val vendas = response.body()?.vendas
                            val aaChartView = root.findViewById<AAChartView>(R.id.vendas_barras)
                            val aaChartModel : AAChartModel = AAChartModel()
                                .chartType(AAChartType.Bar)
                                .title("Vendas por Mes e Ano")
                                .backgroundColor("#ffffff")
                                .dataLabelsEnabled(true)

                            val arrayColunas = mutableListOf<AASeriesElement>()
                            for(venda in vendas!!){
                                val elemento = AASeriesElement()
                                    .name(venda.nomePlanta)
                                    .data(arrayOf(venda.quantidaVenda))

                                arrayColunas.add(elemento)
                            }
                            aaChartModel.series(arrayColunas.toTypedArray())
                            aaChartView.aa_drawChartWithChartModel(aaChartModel)

                        } else {
                            Toast.makeText(
                                this@HomeFragment.requireContext(),
                                getString(R.string.erro_api),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    else {
                        Toast.makeText(
                            this@HomeFragment.requireContext(),
                            getString(R.string.erro_pedido),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                override fun onFailure(call: Call<RespostaVenda>, t: Throwable) {
                    Log.i("PLANTACOES",  t.message.toString())
                    Toast.makeText(this@HomeFragment.requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        if(email != "") {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getAllPlantacoes(email = email)
            call.enqueue(object : Callback<RespostaPlantacoes> {
                override fun onResponse(call: Call<RespostaPlantacoes>, response: Response<RespostaPlantacoes>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            val plantacoes = response.body()?.plantacoes
                            val tamanho = response.body()?.plantacoes?.size
                            if (tamanho != null){
                                val last_plantacao = plantacoes?.last()
                                val sec_last_plantacao = plantacoes?.get(tamanho - 2)
                                plantacao1.text = last_plantacao?.nome
                                plantacao2.text = sec_last_plantacao?.nome
                            }

                        } else {
                            Toast.makeText(
                                    this@HomeFragment.requireContext(),
                                    getString(R.string.erro_api),
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    else {
                        Toast.makeText(
                                this@HomeFragment.requireContext(),
                                getString(R.string.erro_pedido),
                                Toast.LENGTH_LONG
                        ).show()
                    }
                }
                override fun onFailure(call: Call<RespostaPlantacoes>, t: Throwable) {
                    Log.i("PLANTACOES",  t.message.toString())
                    Toast.makeText(this@HomeFragment.requireContext(), t.message, Toast.LENGTH_SHORT).show()
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

//Graficos
/*val aaChartView = root.findViewById<AAChartView>(R.id.vendas_barras)

val aaChartModel : AAChartModel = AAChartModel()
        .chartType(AAChartType.Bar)
        .title("Vendas por Mes e Ano")
        .backgroundColor("#ffffff")
        .dataLabelsEnabled(true)
        .series(arrayOf(
                AASeriesElement()
                        .name("Tokyo")
                        .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
                AASeriesElement()
                        .name("NewYork")
                        .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)),
                AASeriesElement()
                        .name("London")
                        .data(arrayOf(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)),
                AASeriesElement()
                        .name("Berlin")
                        .data(arrayOf(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
        )
        )

aaChartView.aa_drawChartWithChartModel(aaChartModel)*/