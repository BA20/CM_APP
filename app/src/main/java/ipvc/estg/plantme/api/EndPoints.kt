package ipvc.estg.plantme.api

import ipvc.estg.plantme.api.respostas.RespostaEventosPlantacao
import ipvc.estg.plantme.api.respostas.RespostaPlantacoes
import ipvc.estg.plantme.api.respostas.RespostaProduto
import ipvc.estg.plantme.api.respostas.RespostaSugestao
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<User>

    @GET("/plantacoes/getAll/{email}")
    fun getAllPlantacoes(@Path("email") email: String?) : Call<RespostaPlantacoes>

    @GET("/produtos/getById/{id}")
    fun getProdutoId(@Path("id") id: Int?) : Call<RespostaProduto>

    @GET("/eventos/getLatest/{id}")
    fun getEventosRecentes(@Path("id") id: Int?) : Call<RespostaEventosPlantacao>

    @GET("/sugestoes/getEpocaAtual")
    fun getEpocaAtual(): Call<RespostaSugestao>
}