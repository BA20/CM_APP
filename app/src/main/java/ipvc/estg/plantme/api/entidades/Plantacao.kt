package ipvc.estg.plantme.api.entidades

import java.io.Serializable

class Plantacao (
    val id_plantacao: Int,
    val nome: String,
    val area: Float,
    val descricao: String,
    val stock: Int,
    val id_user: Int,
    val id_produto: Int,
    val nomePlanta: String
): Serializable