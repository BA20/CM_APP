package ipvc.estg.plantme.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ipvc.estg.plantme.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
<<<<<<< Updated upstream
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
=======
        sharedPreferences = this.requireActivity().getSharedPreferences(getString(R.string.plantme), Context.MODE_PRIVATE)
        email = sharedPreferences.getString(getString(R.string.email_sp), "").toString()
        nomePlantacao1 = root.findViewById(R.id.plantacao1)
        nomePlantacao2 = root.findViewById(R.id.plantacao2)
        imagePlantacao1 = root.findViewById(R.id.imagePlantacao1)
        imagePlantacao2 = root.findViewById(R.id.imagePlantacao2)

        imagePlantacao1.setOnClickListener {
            val intent = Intent(this.requireContext(), DetalhesPlantacao::class.java)
            intent.putExtra("plantacao", plantacao1)
            startActivity(intent)
        }
        
        imagePlantacao2.setOnClickListener {
            val intent = Intent(this.requireContext(), DetalhesPlantacao::class.java)
            intent.putExtra("plantacao", plantacao2)
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
                                .title("Vendas do Mês Atual")
                                .backgroundColor("#ffffff")
                                .dataLabelsEnabled(true)
                                .xAxisLabelsEnabled(false)
                                .yAxisTitle("Quantidade Vendida")

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
                                plantacao1 = plantacoes?.last()!!
                                plantacao2 = plantacoes[tamanho - 2]
                                nomePlantacao2.text = plantacao2?.nome
                                nomePlantacao1.text = plantacao1?.nome
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
}
>>>>>>> Stashed changes
