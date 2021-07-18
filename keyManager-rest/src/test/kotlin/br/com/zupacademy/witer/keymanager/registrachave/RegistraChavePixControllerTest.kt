package br.com.zupacademy.witer.keymanager.registrachave

import br.com.zupacademy.witer.KeyManagerRegistraGRPCServiceGrpc
import br.com.zupacademy.witer.RegistraChavePixResponse
import br.com.zupacademy.witer.keymanager.compartilhado.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistraChavePixControllerTest {

    //mock stub
    @field:Inject
    lateinit var registraStub: KeyManagerRegistraGRPCServiceGrpc.KeyManagerRegistraGRPCServiceBlockingStub

    //http client
    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve registrar chavePix`() {

        //cenario
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RegistraChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setChavePixId(pixId)
            .build()


        given(registraStub.registra(any())).willReturn(respostaGrpc)

        //ação
        val request = HttpRequest.POST("/api/v1/clientes/$clienteId/pix", novaChavePixRequest())
        val response = client.toBlocking().exchange(request, NovaChavePixRequest::class.java)

        //validação
        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))
    }

    private fun novaChavePixRequest() = NovaChavePixRequest(tipoConta = TipoDeContaRequest.CONTA_CORRENTE,
        chave = "teste@teste.com.br",
        tipoChave = TipoDeChaveRequest.EMAIL)

    //mock do cliente gRPC.
    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() =
            mock(KeyManagerRegistraGRPCServiceGrpc.KeyManagerRegistraGRPCServiceBlockingStub::class.java)
    }
}