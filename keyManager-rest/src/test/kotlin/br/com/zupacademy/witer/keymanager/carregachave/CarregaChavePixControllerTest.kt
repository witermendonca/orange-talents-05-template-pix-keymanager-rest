package br.com.zupacademy.witer.keymanager.carregachave

import br.com.zupacademy.witer.CarregaChavePixResponse
import br.com.zupacademy.witer.KeyManagerCarregaGRPCServiceGrpc.*
import br.com.zupacademy.witer.RemoveChavePixResponse
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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.BDDMockito.*
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class CarregaChavePixControllerTest {

    //mock stub
    @field:Inject
    lateinit var carregaStub: KeyManagerCarregaGRPCServiceBlockingStub

    //http client
    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun `deve carregar informacoes de uma chave pix pelo clienteId e chavePixId`() {

        //cenario
        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(carregaStub.carrega(any())).willReturn(carregaChavePixResponse(clienteId, pixId))

        //ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        with(response) {
            assertEquals(HttpStatus.OK, status)
            assertNotNull(body())
        }

    }

    @Test
    fun `deve carregar informacoes de uma chave pix pela chavePix`() {

        //cenario
        val chavePix = "33059192057"

        given(carregaStub.carrega(any())).willReturn(carregaChavePixPelaChaveResponse(chavePix))

        //ação
        val request = HttpRequest.GET<Any>("/api/v1/clientes/pix/chave/$chavePix")
        val response = client.toBlocking().exchange(request, Any::class.java)

        //validação
        with(response) {
            assertEquals(HttpStatus.OK, status)
            assertNotNull(body())
        }

    }


    private fun carregaChavePixResponse(clienteId: String, pixId: String) =
        CarregaChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .setChavePix(CarregaChavePixResponse.ChavePix
                .newBuilder()
                .setTipo(TipoChave.EMAIL)
                .setChave("teste@teste.com.br")
                .setConta(CarregaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipo(TipoConta.CONTA_CORRENTE)
                    .setInstituicao("ITAÚ UNIBANCO S.A.")
                    .setNomeDoTitular("Witer Mendonça")
                    .setCpfDoTitular("85964254039")
                    .setAgencia("0001")
                    .setNumeroDaConta("123499")
                    .build()
                )
                .setCriadaEm(LocalDateTime.now().let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                })).build()

    private fun carregaChavePixPelaChaveResponse(chavePix: String) =
        CarregaChavePixResponse.newBuilder()
            .setChavePix(CarregaChavePixResponse.ChavePix
                .newBuilder()
                .setTipo(TipoChave.CPF)
                .setChave(chavePix)
                .setConta(CarregaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipo(TipoConta.CONTA_CORRENTE)
                    .setInstituicao("ITAÚ UNIBANCO S.A.")
                    .setNomeDoTitular("Steve Jobs")
                    .setCpfDoTitular("33059192057")
                    .setAgencia("0001")
                    .setNumeroDaConta("123456")
                    .build()
                )
                .setCriadaEm(LocalDateTime.now().let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                })).build()


    //mock do cliente gRPC.
    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {
        @Singleton
        fun stubMock() =
            mock(KeyManagerCarregaGRPCServiceBlockingStub::class.java)
    }
}