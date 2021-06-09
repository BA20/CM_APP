package ipvc.estg.plantme.api.entidades

import java.io.Serializable

class Produto (
    val idProduto: Int,
    val nomePlanta: String,
    val especie: String,
    val tempoGestacao: String,
    val preco: Float
    ): Serializable