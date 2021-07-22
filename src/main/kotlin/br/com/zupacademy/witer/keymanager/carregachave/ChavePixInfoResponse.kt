package br.com.zupacademy.witer.keymanager.carregachave

import br.com.zupacademy.witer.CarregaChavePixResponse
import br.com.zupacademy.witer.TipoConta
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ChavePixInfoResponse(grpcResponse: CarregaChavePixResponse) {

    val clienteId = grpcResponse.clienteId
    val pixId = grpcResponse.pixId
    val chavePix = mapOf(
        Pair("tipoChave", grpcResponse.chavePix.tipo),
        Pair("chave", grpcResponse.chavePix.chave),
        Pair("conta", conta(grpcResponse))
    )
    val criadaEm = grpcResponse.chavePix.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

    private fun tipoConta(grpcResponse: CarregaChavePixResponse) =
        when (grpcResponse.chavePix.conta.tipo) {
            TipoConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
            TipoConta.CONTA_POUPANCA -> "CONTA_POUPANCA"
            else -> "NAO_RECONHECIDA"
        }

    private fun conta(grpcResponse: CarregaChavePixResponse) =
        mapOf(Pair("tipo", tipoConta(grpcResponse)),
            Pair("instituicao", grpcResponse.chavePix.conta.instituicao),
            Pair("nomeDoTitular", grpcResponse.chavePix.conta.nomeDoTitular),
            Pair("cpfDoTitular", grpcResponse.chavePix.conta.cpfDoTitular),
            Pair("agencia", grpcResponse.chavePix.conta.agencia),
            Pair("numero", grpcResponse.chavePix.conta.numeroDaConta))

}

//{
//    "clienteId": "5260263c-a3c1-4727-ae32-3bdb2538841b",
//    "pixId": "3bbf8e28-3d99-48c5-8310-478ea6e4299a",
//    "chavePix": {
//    "tipo": "CELULAR",
//    "chave": "+5585988714077",
//    "conta": {
//    "tipo": "CONTA_POUPANCA",
//    "instituicao": "ITAÚ UNIBANCO S.A.",
//    "nomeDoTitular": "Yuri Matheus",
//    "cpfDoTitular": "86135457004",
//    "agencia": "0001",
//    "numeroDaConta": "291900"
//},
//    "criadaEm": {
//    "seconds": "1626687611",
//    "nanos": 464292000
//}
//}
//}