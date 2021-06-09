package ipvc.estg.plantme.api.respostas

import ipvc.estg.plantme.api.entidades.Evento

class RespostaEventosPlantacao(
    val status: Boolean,
    val eventos: List<Evento>
)