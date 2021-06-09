package ipvc.estg.plantme.api.respostas

import ipvc.estg.plantme.api.entidades.Sugestao

class RespostaSugestao(
        val status: Boolean,
        val sugestoes: List<Sugestao>
)