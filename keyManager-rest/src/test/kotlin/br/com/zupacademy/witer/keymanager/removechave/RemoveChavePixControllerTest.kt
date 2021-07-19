package br.com.zupacademy.witer.keymanager.removechave

import br.com.zupacademy.witer.KeyManagerRemoveGRPCServiceGrpc.KeyManagerRemoveGRPCServiceBlockingStub
import br.com.zupacademy.witer.RemoveChavePixResponse
import br.com.zupacademy.witer.keymanager.compartilhado.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveChavePixControllerTest {

    //mock stub
    @field:Inject
    lateinit var removeStub: KeyManagerRemoveGRPCServiceBlockingStub

    //http client
    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient


    @Test
    fun `deve remover chavePix existente`() {

        //cenario
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RemoveChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setChavePixId(pixId)
            .build()

        given(removeStub.remove(any())).willReturn(respostaGrpc)

        //ação
        val request = HttpRequest.DELETE<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        //validação
        assertEquals(HttpStatus.OK, response.status)
    }


    //mock do cliente gRPC.
    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() = mock(KeyManagerRemoveGRPCServiceBlockingStub::class.java)
    }
}