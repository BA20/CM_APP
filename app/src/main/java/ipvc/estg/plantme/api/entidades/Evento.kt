package ipvc.estg.plantme.api.entidades

import java.io.Serializable
import java.util.*

class Evento (
    val id_evento: Int,
    val tipo: Int,
    val stock_adicionado: Int,
    val data: Date,
    val id_plantacao: Int
    ): Serializable