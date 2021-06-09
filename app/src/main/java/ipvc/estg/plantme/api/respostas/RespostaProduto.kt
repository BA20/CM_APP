package ipvc.estg.plantme.api.respostas

import ipvc.estg.plantme.api.entidades.Produto

class RespostaProduto(
    val status: Boolean,
    val produto: Produto
)