package com.david.servidor.controller.participation

import com.david.servidor.controller.activity.ActivityResponse
import com.david.servidor.controller.user.UserResponse
import com.david.servidor.model.Activity
import com.david.servidor.model.Participation
import com.david.servidor.model.User
import com.david.servidor.service.ParticipationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/participation")
class ParticipationController(private val participationService: ParticipationService) {

    // Registrar una nueva participación
    @PostMapping
    fun participate(@RequestBody request: ParticipationRequest): ResponseEntity<ParticipationResponse> {
        val participation = participationService.participate(request.userId, request.activityId)
        return ResponseEntity.ok(participation.toResponse())
    }

    // Obtener todas las participaciones
    @GetMapping
    fun listAll(): List<ParticipationResponse> = participationService.findAll().map { it.toResponse() }

    // Obtener todas las actividades en las que participa un usuario
    @GetMapping("/user/{userId}")
    fun getUserParticipations(@PathVariable userId: String): List<ActivityResponse> =
        participationService.getUserParticipations(userId).map { it.toResponse() }

    // Obtener todos los usuarios que participan en una actividad
    @GetMapping("/activity/{activityId}")
    fun getActivityParticipants(@PathVariable activityId: String): List<UserResponse> =
        participationService.getActivityParticipants(activityId).map { it.toResponse() }

    // Eliminar una participación por su ID
    @DeleteMapping("/{activityId}/{userId}")
    fun deleteParticipation(@PathVariable activityId: String, @PathVariable userId: String): Boolean {
        val allParticipations = participationService.findAll()
        val participation = allParticipations.find { it.activityId == activityId.toLong() && it.userId == userId }
        return if (participation != null) {
            participationService.deleteParticipation(participation.id!!)
        } else {
            false
        }
    }

    private fun Participation.toResponse(): ParticipationResponse =
        ParticipationResponse(
            id = this.id.toString(),
            userId = this.userId,
            activityId = this.activityId
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

    private fun User.toResponse(): UserResponse =
        UserResponse(
            id = this.id.toString(),
            email = this.email,
            role = this.role.toString()
        )
}