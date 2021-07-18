package br.com.zupacademy.witer.keymanager.registrachave

import br.com.zupacademy.witer.TipoConta

enum class TipoDeContaRequest(val atributoGrpc: TipoConta) {

    CONTA_CORRENTE(TipoConta.CONTA_CORRENTE),

    CONTA_POUPANCA(TipoConta.CONTA_POUPANCA)
}