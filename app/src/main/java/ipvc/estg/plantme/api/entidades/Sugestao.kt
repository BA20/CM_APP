package ipvc.estg.plantme.api.entidades

import java.io.Serializable
import java.util.*

class Sugestao (
        val idSugestao: Int,
        val nomePlanta: String,
        val descricao: String,
        val data_inicio: Date,
        val data_fim: Date
): Serializable