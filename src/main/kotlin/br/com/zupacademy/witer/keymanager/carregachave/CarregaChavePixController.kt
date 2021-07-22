package br.com.zupacademy.witer.keymanager.carregachave

import br.com.zupacademy.witer.CarregaChavePixRequest
import br.com.zupacademy.witer.KeyManagerCarregaGRPCServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Inject

@Controller
class CarregaChavePixController(
    @Inject private val carregaChavePixClient: KeyManagerCarregaGRPCServiceGrpc.KeyManagerCarregaGRPCServiceBlockingStub,
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Get("/api/v1/clientes/{clienteId}/pix/{chavePixId}")
    fun carregaChavePix(
        @PathVariable("clienteId") clienteId: UUID,
        @PathVariable("chavePixId") chavePixId: UUID,
    ): HttpResponse<Any> {

        logger.info("[ClienteID: $clienteId] carregando informações chave pix com ID: $chavePixId")
        val grpcResponse = carregaChavePixClient.carrega(CarregaChavePixRequest.newBuilder()
            .setPixEClienteId(CarregaChavePixRequest.FiltroPorPixEClienteId.newBuilder()
                .setClienteId(clienteId.toString())
                .setPixId(chavePixId.toString())
                .build())
            .build())

        logger.info("Informações Chave Pix carregada. $chavePixId")
        return HttpResponse.ok(ChavePixInfoResponse(grpcResponse))
    }

    @Get("/api/v1/clientes/pix/chave/{chavePix}")
    fun carregaChavePixPelaChave(@PathVariable("chavePix") chavePix: String): HttpResponse<Any> {

        logger.info("[Chave Pix: $chavePix] carregando informações chave pix.")
        val grpcResponse = carregaChavePixClient.carrega(CarregaChavePixRequest.newBuilder()
            .setChave(chavePix)
            .build())

        logger.info("Informações Chave Pix carregada. $chavePix")
        return HttpResponse.ok(ChavePixInfoResponse(grpcResponse))
    }
}