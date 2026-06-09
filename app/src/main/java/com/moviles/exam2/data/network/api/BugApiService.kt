package com.moviles.exam2.data.network.api

import com.moviles.exam2.data.network.dto.BugDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz de Retrofit que define los endpoints de la API de ButterTech.
 * Basado en el contrato definido en /contracts/bugs-api.yaml.
 */
interface BugApiService {

    @GET("bugs")
    suspend fun getBugs(): Response<List<BugDto>>

    @POST("bugs")
    suspend fun createBug(@Body bug: BugDto): Response<BugDto>

    @PATCH("bugs/{id}")
    suspend fun updateBug(
        @Path("id") id: String,
        @Body updates: Map<String, String>
    ): Response<BugDto>
}
