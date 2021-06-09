package ipvc.estg.plantme.ui.plantacoes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import ipvc.estg.plantme.R
import ipvc.estg.plantme.api.EndPoints
import ipvc.estg.plantme.api.ServiceBuilder
import ipvc.estg.plantme.api.entidades.Evento
import ipvc.estg.plantme.api.entidades.Plantacao
import ipvc.estg.plantme.api.entidades.Produto
import ipvc.estg.plantme.api.respostas.RespostaEventosPlantacao
import ipvc.estg.plantme.api.respostas.RespostaProduto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetalhesPlantacao : AppCompatActivity(), EventosPlantacoesAdaptor.OnNotaClickListener {

    private lateinit var nomeZona: TextView
    private lateinit var nomeProd: TextView
    private lateinit var nomeEspecie: TextView
    private lateinit var stock: TextView
    private lateinit var area: TextView
    private lateinit var observacoes: TextView

    private lateinit var plantacao: Plantacao
    private lateinit var produto: Produto

    private lateinit var recyclerViewEventos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_plantacao)

        nomeZona = findViewById(R.id.nome_zona)
        nomeProd = findViewById(R.id.nome_produto)
        nomeEspecie = findViewById(R.id.nome_especie)
        stock = findViewById(R.id.nomeProduto)
        area = findViewById(R.id.dp_area)
        observacoes = findViewById(R.id.observacoes)


        plantacao = intent.getSerializableExtra("plantacao") as Plantacao
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getProdutoId(id = plantacao.id_produto)
        call.enqueue(object : Callback<RespostaProduto> {
            override fun onResponse(call: Call<RespostaProduto>, response: Response<RespostaProduto>) {
                if (response.isSuccessful) {
                    if (response.body()?.status == true) {
                        produto = response.body()?.produto!!
                        nomeZona.text = plantacao.nome
                        nomeProd.text = produto.nomePlanta
                        nomeEspecie.text = produto.especie
                        stock.text = plantacao.stock.toString()
                        area.text = plantacao.area.toString()
                        observacoes.text = plantacao.descricao
                    } else {
                        Toast.makeText(
                            this@DetalhesPlantacao,
                            getString(R.string.erro_api),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                else {
                    Toast.makeText(
                        this@DetalhesPlantacao,
                        getString(R.string.erro_pedido),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onFailure(call: Call<RespostaProduto>, t: Throwable) {
                Toast.makeText(this@DetalhesPlantacao, t.message, Toast.LENGTH_SHORT).show()
            }
        })

        recyclerViewEventos = findViewById(R.id.recycler_eventos)
        val adaptor = EventosPlantacoesAdaptor(this)
        recyclerViewEventos.adapter = adaptor
        recyclerViewEventos.layoutManager = LinearLayoutManager(this)
        recyclerViewEventos.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).build())

        val callEventos = request.getEventosRecentes(id = plantacao.id_plantacao)
        callEventos.enqueue(object : Callback<RespostaEventosPlantacao> {
            override fun onResponse(call: Call<RespostaEventosPlantacao>, response: Response<RespostaEventosPlantacao>) {
                if (response.isSuccessful) {
                    if (response.body()?.status == true) {
                        adaptor.setPlantacoes(response.body()?.eventos!!)
                    } else {
                        Toast.makeText(
                            this@DetalhesPlantacao,
                            getString(R.string.erro_api),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                else {
                    Toast.makeText(
                        this@DetalhesPlantacao,
                        getString(R.string.erro_pedido),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onFailure(call: Call<RespostaEventosPlantacao>, t: Throwable) {
                Toast.makeText(this@DetalhesPlantacao, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onItemClick(item: Evento, position: Int) {

    }

    fun backPlantacoes(view: View) {
        if(view is ImageView) {
            finish()
        }
    }
}