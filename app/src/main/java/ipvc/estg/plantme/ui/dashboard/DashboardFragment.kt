package ipvc.estg.plantme.ui.dashboard

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import ipvc.estg.plantme.R
import ipvc.estg.plantme.api.EndPoints
import ipvc.estg.plantme.api.ServiceBuilder
import ipvc.estg.plantme.api.entidades.Venda
import ipvc.estg.plantme.api.respostas.RespostaVenda
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = this.requireActivity().getSharedPreferences(getString(R.string.plantme), Context.MODE_PRIVATE)
        email = sharedPreferences.getString(getString(R.string.email_sp), "").toString()
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
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
                                .title("Vendas dos Mês Atual")
                                .backgroundColor("#ffffff")
                                .dataLabelsEnabled(true)
                                .yAxisLabelsEnabled(true)
                                .yAxisTitle("Quantidade Vendida")
                                .xAxisLabelsEnabled(false)




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
                                this@DashboardFragment.requireContext(),
                                getString(R.string.erro_api),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    else {
                        Toast.makeText(
                            this@DashboardFragment.requireContext(),
                            getString(R.string.erro_pedido),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                override fun onFailure(call: Call<RespostaVenda>, t: Throwable) {
                    Log.i("PLANTACOES",  t.message.toString())
                    Toast.makeText(this@DashboardFragment.requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })


            val call1 = request.getVendasAll()
            call1.enqueue(object : Callback<RespostaVenda> {
                override fun onResponse(call: Call<RespostaVenda>, response: Response<RespostaVenda>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            val vendas = response.body()?.vendas
                            val aaChartView = root.findViewById<AAChartView>(R.id.vendas_total)
                            val aaChartModel : AAChartModel = AAChartModel()
                                .chartType(AAChartType.Column)
                                .title("Vendas dos Mês Atual")
                                .backgroundColor("#ffffff")
                                .yAxisLabelsEnabled(true)
                                .yAxisTitle("Quantidade Vendida")
                                .xAxisLabelsEnabled(false)
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
                                this@DashboardFragment.requireContext(),
                                getString(R.string.erro_api),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    else {
                        Toast.makeText(
                            this@DashboardFragment.requireContext(),
                            getString(R.string.erro_pedido),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                override fun onFailure(call: Call<RespostaVenda>, t: Throwable) {
                    Log.i("PLANTACOES",  t.message.toString())
                    Toast.makeText(this@DashboardFragment.requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        return root

    }


}