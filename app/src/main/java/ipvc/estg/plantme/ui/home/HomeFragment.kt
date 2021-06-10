package ipvc.estg.plantme.ui.home

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
import ipvc.estg.plantme.api.respostas.RespostaSugestao
import ipvc.estg.plantme.api.respostas.RespostaVenda
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeFragment : Fragment() {

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

        val c = Calendar.getInstance()
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        if(email != "") {
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getVendasMesAno(year, month + 1)
            call.enqueue(object : Callback<RespostaVenda> {
                override fun onResponse(call: Call<RespostaVenda>, response: Response<RespostaVenda>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            val venda = response.body()?.vendas

                            val aaChartView = root.findViewById<AAChartView>(R.id.vendas_barras)

                            for(i in venda!!){

                                Log.d("QUANT", i.quantidade.toString())
                                val aaChartModel : AAChartModel = AAChartModel()
                                        .chartType(AAChartType.Bar)
                                        .title("Vendas por Mes e Ano")
                                        .backgroundColor("#ffffff")
                                        .dataLabelsEnabled(true)
                                        .series(arrayOf(
                                                AASeriesElement()
                                                        .name("Produtos")
                                                        .data(arrayOf(i.quantidade))
                                        )
                                        )

                                aaChartView.aa_drawChartWithChartModel(aaChartModel)
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
                override fun onFailure(call: Call<RespostaVenda>, t: Throwable) {
                    Toast.makeText(this@HomeFragment.requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            })
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

        /*homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }
}