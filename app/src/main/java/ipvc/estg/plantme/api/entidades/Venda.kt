package ipvc.estg.plantme.api.entidades

import java.io.Serializable
import java.util.*

class Venda (
        val idvenda: Int,
        val quantidade: Int,
        val date: Date,
        val id_produto: Produto
): Serializable