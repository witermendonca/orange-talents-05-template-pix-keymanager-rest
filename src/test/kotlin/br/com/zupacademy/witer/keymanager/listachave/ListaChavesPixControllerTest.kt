package br.com.zupacademy.witer.keymanager.listachave

import br.com.zupacademy.witer.KeyManagerListaGRPCServiceGrpc.KeyManagerListaGRPCServiceBlockingStub
import br.com.zupacademy.witer.ListaChavePixResponse
import br.com.zupacademy.witer.TipoChave
import br.com.zupacademy.witer.TipoConta
import br.com.zupacademy.witer.keymanager.compartilhado.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavesPixControllerTest {

    //mock stub
    @field:Inject
    lateinit var listaChaveStub: KeyManagerListaGRPCServiceBlockingStub

    //http client
    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve listar todas as chaves pix existente pelo clienteId`() {

        //cenario
        val clienteId = UUID.randomUUID().toString()
        given(listaChaveStub.lista(Mockito.any())).willReturn(listaChavePixResponse(clienteId))

        //ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/")
        val response = client.toBlocking().exchange(request, List::class.java)

        //validação
        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(response.body()!!.size, 3)
    }

    @Test
    fun `nao deve listar as chavesPix do cliente quando cliente nao possuir chavesPix`() {

        //cenario
        val clienteIdSemChavesPix = UUID.randomUUID().toString()
        given(listaChaveStub.lista(Mockito.any())).willReturn(ListaChavePixResponse.newBuilder()
            .setClienteId(clienteIdSemChavesPix)
            .build())

        //ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteIdSemChavesPix/pix/")
        val response = client.toBlocking().exchange(request, List::class.java)

        //validação(Validar se o número de chavesPix da lista é 0.)
        assertEquals(response.body().size, 0)
    }


    private fun listaChavePixResponse(clienteId: String): ListaChavePixResponse {

        val chaveAleatoria = ListaChavePixResponse.ChavePix.newBuilder()
            .setChavePixId(UUID.randomUUID().toString())
            .setTipoChave(TipoChave.ALEATORIA)
            .setChave(UUID.randomUUID().toString())
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chaveEmail = ListaChavePixResponse.ChavePix.newBuilder()
            .setChavePixId(UUID.randomUUID().toString())
            .setTipoChave(TipoChave.EMAIL)
            .setChave("teste@teste.com.br")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()

        val chaveCpf = ListaChavePixResponse.ChavePix.newBuilder()
            .setChavePixId(UUID.randomUUID().toString())
            .setTipoChave(TipoChave.CPF)
            .setChave("33059192057")
            .setTipoConta(TipoConta.CONTA_CORRENTE)
            .setCriadaEm(LocalDateTime.now().let {
                val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                Timestamp.newBuilder()
                    .setSeconds(createdAt.epochSecond)
                    .setNanos(createdAt.nano)
                    .build()
            })
            .build()


        return ListaChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .addAllChavesPix(listOf(chaveAleatoria, chaveEmail, chaveCpf))
            .build()

    }


    //mock do cliente gRPC.
    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() =
            BDDMockito.mock(KeyManagerListaGRPCServiceBlockingStub::class.java)
    }
}