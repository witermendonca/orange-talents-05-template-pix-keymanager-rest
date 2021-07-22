package br.com.zupacademy.witer.keymanager.removechave

import br.com.zupacademy.witer.KeyManagerRemoveGRPCServiceGrpc
import br.com.zupacademy.witer.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller
class RemoveChavePixController(
    @Inject private val removeChavePixCliente: KeyManagerRemoveGRPCServiceGrpc.KeyManagerRemoveGRPCServiceBlockingStub,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Delete("/api/v1/clientes/{clienteId}/pix/{chavePixId}")
    fun deletaChavePix(
        @PathVariable("clienteId") clienteId: UUID,
        @PathVariable("chavePixId") chavePixId: UUID,
    ): HttpResponse<Any> {

        logger.info("[ClienteID: $clienteId] removendo chave pix com ID: $chavePixId")
        removeChavePixCliente.remove(RemoveChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setChavePixId(chavePixId.toString())
            .build())

        logger.info("Chave Pix removida. $chavePixId")
        return HttpResponse.ok()
    }
}