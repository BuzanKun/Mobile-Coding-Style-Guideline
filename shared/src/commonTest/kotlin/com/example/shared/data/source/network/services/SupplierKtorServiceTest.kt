package com.example.shared.data.source.network.services

import com.example.shared.data.source.network.model.request.supplier.CreateUpdateSupplierBody
import com.example.shared.data.source.network.model.request.supplier.DeleteSupplierBody
import com.example.shared.data.source.network.model.request.supplier.GetSupplierQueryParams
import com.example.shared.data.source.network.model.request.supplier.PatchEditStatusSupplierBody
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class SupplierKtorServiceTest {

    private val rawToken = "Bearer dummy-bearer-token"

    private val expectedAuthHeader = "Bearer $rawToken"

    private fun createServiceWithMockEngine(
        handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ): SupplierKtorService {
        val mockEngine = MockEngine(handler)
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(accessToken = rawToken, refreshToken = "")
                    }
                }
            }
            install(DefaultRequest) {
                contentType(ContentType.Application.Json)
            }
        }
        return SupplierKtorService(httpClient)
    }

    @Test
    fun `getSupplierList SHOULD build a correct GET request with a path parameter`() = runTest {
        // Arrange
        val service = createServiceWithMockEngine { request ->
            assertEquals(HttpMethod.Get, request.method)
            assertEquals("/supplier", request.url.encodedPath)
            assertEquals(expectedAuthHeader, request.headers["Authorization"])
            respond("{}", headers = headersOf("Content-Type", "application/json"))
        }

        // Act
        val response = service.getSuppliers(GetSupplierQueryParams().toQueryMap())

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `getSupplierById SHOULD build a correct GET request with a path parameter`() = runTest {
        // Arrange
        val supplierId = "test-id-123"
        val service = createServiceWithMockEngine { request ->
            assertEquals(HttpMethod.Get, request.method)
            assertEquals("/supplier/$supplierId", request.url.encodedPath)
            assertEquals(expectedAuthHeader, request.headers["Authorization"])
            respond("{}", headers = headersOf("Content-Type", "application/json"))
        }

        // Act
        val response = service.getSupplierById(supplierId)

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `getSupplierOption SHOULD build a correct GET request with query parameters`() = runTest {
        // Arrange
        val query = mapOf("cityOption" to true, "supplierOption" to false)
        val service = createServiceWithMockEngine { request ->
            assertEquals(HttpMethod.Get, request.method)
            assertEquals("/supplier/option", request.url.encodedPath)
            assertEquals("true", request.url.parameters["cityOption"])
            assertEquals("false", request.url.parameters["supplierOption"])
            assertEquals(expectedAuthHeader, request.headers["Authorization"])
            respond("{}", headers = headersOf("Content-Type", "application/json"))
        }

        // Act
        val response = service.getSupplierOption(query)

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `deleteSupplier SHOULD build a correct DELETE request with a body`() = runTest {
        // Arrange
        val requestBody = DeleteSupplierBody(supplierID = listOf("id1", "id2"))
        val expectedJsonBody = Json.encodeToString(requestBody)
        val service = createServiceWithMockEngine { request ->
            assertEquals(HttpMethod.Delete, request.method)
            assertEquals("/supplier", request.url.encodedPath)
            assertEquals(expectedAuthHeader, request.headers["Authorization"])
            val actualBody = (request.body as TextContent).text
            assertEquals(expectedJsonBody, actualBody)
            respond("", HttpStatusCode.OK)
        }

        // Act
        val response = service.deleteSupplier(requestBody)

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `editSupplier SHOULD build a correct PUT request with a path and body`() = runTest {
        // Arrange
        val supplierId = "test-id-123"
        val requestBody = CreateUpdateSupplierBody(companyName = "Updated Corp")
        val json = Json { encodeDefaults = true }
        val expectedJsonBody = json.encodeToString(requestBody)
        val service = createServiceWithMockEngine { request ->
            assertEquals(HttpMethod.Put, request.method)
            assertEquals("/supplier/$supplierId", request.url.encodedPath)
            assertEquals(expectedAuthHeader, request.headers["Authorization"])
            val actualBody = (request.body as TextContent).text
            assertEquals(expectedJsonBody, actualBody)
            respond("{}", HttpStatusCode.OK)
        }

        // Act
        val response = service.editSupplier(supplierId, requestBody)

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `editStatusSupplier SHOULD build a correct PATCH request with a body`() = runTest {
        // Arrange
        val requestBody = PatchEditStatusSupplierBody(supplierID = listOf("id1"), status = true)
        val expectedJsonBody = Json.encodeToString(requestBody)
        val service = createServiceWithMockEngine { request ->
            assertEquals(HttpMethod.Patch, request.method)
            assertEquals("/supplier", request.url.encodedPath)
            assertEquals(expectedAuthHeader, request.headers["Authorization"])
            val actualBody = (request.body as TextContent).text
            assertEquals(expectedJsonBody, actualBody)
            respond("{}", HttpStatusCode.OK)
        }

        // Act
        val response = service.editStatusSupplier(requestBody)

        // Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }
}