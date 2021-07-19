package br.com.zupacademy.witer.keymanager.registrachave

import br.com.zupacademy.witer.KeyManagerRegistraGRPCServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller
class RegistraChavePixController(
    @Inject private val registraChavePixCliente: KeyManagerRegistraGRPCServiceGrpc.KeyManagerRegistraGRPCServiceBlockingStub,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Post("/api/v1/clientes/{clienteId}/pix")
    fun registraChavePix(
        @PathVariable("clienteId") clienteId: UUID,
        @Valid @Body chavePixRequest: NovaChavePixRequest,
    ): HttpResponse<Any> {

        logger.info("[ClienteID: $clienteId] criando uma nova chave pix com ${chavePixRequest}")

        val grpcResponse = registraChavePixCliente.registra(chavePixRequest.paraModelGrpc(clienteId))

        logger.info("ChavePix criada com Id: ${grpcResponse.chavePixId}")
        return HttpResponse.created(location(clienteId = clienteId, pixId = grpcResponse.chavePixId))

    }

    private fun location(clienteId: UUID, pixId: String) = HttpResponse
        .uri("/api/v1/clientes/$clienteId/pix/${pixId}")

}




