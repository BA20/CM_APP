package ipvc.estg.plantme.api.respostas

import ipvc.estg.plantme.api.entidades.Plantacao

class RespostaPlantacoes (
    val status: Boolean,
    val plantacoes: List<Plantacao>
)