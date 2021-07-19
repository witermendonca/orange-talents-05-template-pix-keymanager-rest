package br.com.zupacademy.witer.keymanager.compartilhado.grpc

import br.com.zupacademy.witer.KeyManagerCarregaGRPCServiceGrpc
import br.com.zupacademy.witer.KeyManagerListaGRPCServiceGrpc
import br.com.zupacademy.witer.KeyManagerRegistraGRPCServiceGrpc
import br.com.zupacademy.witer.KeyManagerRemoveGRPCServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registraChavePix() = KeyManagerRegistraGRPCServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeChavePix() = KeyManagerRemoveGRPCServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun carregaInfoChavePix() = KeyManagerCarregaGRPCServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaInfoChavesPix() = KeyManagerListaGRPCServiceGrpc.newBlockingStub(channel)
}