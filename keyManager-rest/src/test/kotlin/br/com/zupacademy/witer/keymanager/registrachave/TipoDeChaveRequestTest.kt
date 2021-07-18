package br.com.zupacademy.witer.keymanager.registrachave

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TipoDeChaveRequestTest {

    @Nested
    inner class CPF {

        @Test
        fun `deve ser valido quando cpf for um numero valido`() {

            val tipoDechave = TipoDeChaveRequest.CPF

            assertTrue(tipoDechave.valida("704.844.250-64"))
        }

        @Test
        fun `nao deve ser valido quando cpf for um numero invalido`() {

            val tipoDechave = TipoDeChaveRequest.CPF

            assertFalse(tipoDechave.valida("000.000.000-09"))
        }

        @Test
        fun `nao deve ser valido quando cpf nao for informado`() {

            val tipoDechave = TipoDeChaveRequest.CPF

            assertFalse(tipoDechave.valida(null))
            assertFalse(tipoDechave.valida(""))
        }

        @Test
        fun `nao deve ser valido quando cpf possuir letras`() {
            with(TipoDeChaveRequest.CPF) {
                assertFalse(valida("704.844.250-6a"))
            }
        }
    }

    @Nested
    inner class CELULAR {

        @Test
        fun `deve ser valido quando celular for um numero valido`() {

            val tipoDechave = TipoDeChaveRequest.CELULAR

            assertTrue(tipoDechave.valida("+5585988714077"))
        }

        @Test
        fun `nao deve ser valido quando celular for um numero invalido`() {

            val tipoDechave = TipoDeChaveRequest.CELULAR

            assertFalse(tipoDechave.valida("5585988714077"))
            assertFalse(tipoDechave.valida("+55a5988714077"))
        }

        @Test
        fun `nao deve ser valido quando celular for um numero nao for informado`() {

            val tipoDechave = TipoDeChaveRequest.CELULAR

            assertFalse(tipoDechave.valida(null))
            assertFalse(tipoDechave.valida(""))
        }
    }

    @Nested
    inner class EMAIL {

        @Test
        fun `deve ser valido quando email for endereco valido`() {

            val tipoDechave = TipoDeChaveRequest.EMAIL

            assertTrue(tipoDechave.valida("witer.mendonca@zup.com.br"))
        }

        @Test
        fun `nao deve ser valido quando email estiver em um formato invalido`() {

            val tipoDechave = TipoDeChaveRequest.EMAIL

            assertFalse(tipoDechave.valida("witer.mendoncazup.com.br"))
            assertFalse(tipoDechave.valida("witer.mendonca@zup.com."))
        }

        @Test
        fun `nao deve ser valido quando email nao for informado`() {

            val tipoDechave = TipoDeChaveRequest.EMAIL

            assertFalse(tipoDechave.valida(null))
            assertFalse(tipoDechave.valida(""))
        }
    }

    @Nested
    inner class ALEATORIA {

        @Test
        fun `deve ser valido quando chave aleatoria for nula ou vazia`() {

            val tipoDechave = TipoDeChaveRequest.ALEATORIA

            assertTrue(tipoDechave.valida(null))
            assertTrue(tipoDechave.valida(""))
        }

        @Test
        fun `nao deve ser valido quando chave aleatoria possuir um valor`() {

            val tipoDechave = TipoDeChaveRequest.ALEATORIA

            assertFalse(tipoDechave.valida("qualquer valor CPF, Email, Celular."))
        }
    }
}