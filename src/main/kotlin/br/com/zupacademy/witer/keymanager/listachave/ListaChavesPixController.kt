package br.com.zupacademy.witer.keymanager.listachave

import br.com.zupacademy.witer.KeyManagerListaGRPCServiceGrpc
import br.com.zupacademy.witer.ListaChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller
class ListaChavesPixController(
    @Inject private val listaChavesPixClient: KeyManagerListaGRPCServiceGrpc.KeyManagerListaGRPCServiceBlockingStub,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Get("/api/v1/clientes/{clienteId}/pix/")
    fun listaChavesPixDeUmClienteId(@PathVariable("clienteId") clienteId: UUID): HttpResponse<Any> {

        logger.info("[ClienteID: $clienteId] Listando informações chaves pix.")
        val grpcResponse = listaChavesPixClient.lista(ListaChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .build())

        return HttpResponse.ok(grpcResponse.chavesPixList.map { ChavePixResponse(it) })
    }
}
