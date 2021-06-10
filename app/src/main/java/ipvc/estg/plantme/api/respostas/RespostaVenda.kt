package ipvc.estg.plantme.api.respostas

import ipvc.estg.plantme.api.entidades.Venda

class RespostaVenda(
        val status: Boolean,
        val vendas: List<Venda>
)