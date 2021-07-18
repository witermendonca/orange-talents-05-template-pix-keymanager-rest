package br.com.zupacademy.witer.keymanager.compartilhado.grpc

import br.com.zupacademy.witer.KeyManagerRegistraGRPCServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registraChavePix() = KeyManagerRegistraGRPCServiceGrpc.newBlockingStub(channel)
}