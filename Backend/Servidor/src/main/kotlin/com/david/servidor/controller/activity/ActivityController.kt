package com.david.servidor.controller.activity

import com.david.servidor.model.Activity
import com.david.servidor.service.ActivityService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/activity")
class ActivityController(private val activityService: ActivityService) {

    // Crear una nueva actividad
    @PostMapping
    fun create(@RequestBody request: ActivityRequest): ResponseEntity<ActivityResponse> {
        val activity = activityService.create(request.toModel())
        return ResponseEntity.ok(activity.toResponse())
    }

    // Obtener todas las actividades
    @GetMapping
    fun listAll(): List<ActivityResponse> =
        activityService.findAll().map { it.toResponse() }

    // Buscar una actividad por su ID
    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<ActivityResponse> {
        val activity = activityService.findById(id)
        return if (activity != null) {
            ResponseEntity.ok(activity.toResponse())
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    // Buscar actividades por nombre
    @GetMapping("/name/{name}")
    fun findByName(@PathVariable name: String): List<ActivityResponse> =
        activityService.findByName(name).map { it.toResponse() }

    // Buscar actividades por lugar
    @GetMapping("/place/{place}")
    fun findByPlace(@PathVariable place: String): List<ActivityResponse> =
        activityService.findByPlace(place).map { it.toResponse() }

    // Eliminar una actividad por su ID
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: String): ResponseEntity<Void> {
        return if (activityService.deleteById(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    private fun ActivityRequest.toModel(): Activity =
        Activity(
            id = null,
            name = this.name,
            description = this.description,
            startDate = this.startDate,
            endDate = this.endDate,
            place = this.place
        )

    private fun Activity.toResponse(): ActivityResponse =
        ActivityResponse(
            id = this.id.toString(),
            name = this.name,
            description = this.description,
            startDate = this.startDate,
            endDate = this.endDate,
            place = this.place
        )
}