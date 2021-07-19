package br.com.zupacademy.witer.keymanager.listachave

import br.com.zupacademy.witer.ListaChavePixResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ChavePixResponse(chavePix: ListaChavePixResponse.ChavePix) {

    val chavePixId = chavePix.chavePixId
    val tipoChave = chavePix.tipoChave
    val chave = chavePix.chave
    val tipoConta = chavePix.tipoConta
    val criadaEm = chavePix.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

}

//{
//    "chavePixId": "150fb1c5-907c-49d1-b61b-ec6f7385a92d",
//    "tipoChave": "ALEATORIA",
//    "chave": "66f7c222-b62a-4287-bf83-bd59b6092f3f",
//    "tipoConta": "CONTA_POUPANCA",
//    "criadaEm": {
//    "seconds": "1626696764",
//    "nanos": 880625000
//}
//},