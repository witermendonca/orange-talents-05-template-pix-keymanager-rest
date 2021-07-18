package br.com.zupacademy.witer.keymanager.registrachave

import br.com.zupacademy.witer.RegistraChavePixRequest
import br.com.zupacademy.witer.TipoChave
import br.com.zupacademy.witer.TipoConta
import br.com.zupacademy.witer.keymanager.compartilhado.validations.ValidPixKey
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
data class NovaChavePixRequest(
    @field:NotNull val tipoConta: TipoDeContaRequest?,
    @field:Size(max = 77) val chave: String?,
    @field:NotNull val tipoChave: TipoDeChaveRequest?,
) {
    fun paraModelGrpc(clienteId: UUID): RegistraChavePixRequest {
        return RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoConta(tipoConta?.atributoGrpc ?: TipoConta.TIPO_CONTA_DESCONHECIDA)
            .setTipoChave(tipoChave?.atributoGrpc ?: TipoChave.TIPO_CHAVE_DESCONHECIDA)
            .setChave(chave ?: "")
            .build()
    }

}

