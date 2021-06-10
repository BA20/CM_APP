package ipvc.estg.plantme.api.entidades

import java.io.Serializable
import java.util.*

class Venda (
        val idVenda: Int,
        val quantidade: Int,
        val date: Date,
        val idProduto: Produto
): Serializable