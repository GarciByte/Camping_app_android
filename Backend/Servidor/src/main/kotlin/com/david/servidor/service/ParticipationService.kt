package com.david.servidor.service

import com.david.servidor.model.Activity
import com.david.servidor.model.Participation
import com.david.servidor.model.User
import com.david.servidor.repository.ParticipationRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ParticipationService(
    private val participationRepository: ParticipationRepository,
    private val activityService: ActivityService,
    private val userService: UserService
) {
    // Registrar una nueva participación
    fun participate(userId: String, activityId: Long): Participation {
        userService.findById(userId) ?: throw IllegalArgumentException("User not found")
        activityService.findById(activityId.toString()) ?: throw IllegalArgumentException("Activity not found")
        return participationRepository.save(Participation(userId = userId, activityId = activityId))
            ?: throw IllegalArgumentException("User is already participating in this activity")
    }

    // Obtener todas las participaciones
    fun findAll(): List<Participation> = participationRepository.findAll()

    // Obtener todas las participaciones de un usuario
    fun getUserParticipations(userId: String): List<Activity> {
        val participations = participationRepository.findByUserId(userId)
        val activities = participations.mapNotNull { participation ->
            activityService.findById(participation.activityId.toString())
        }

        return activities
    }

    // Obtener todas las participaciones de una actividad
    fun getActivityParticipants(activityId: String): List<User> {
        val participations = participationRepository.findByActivityId(activityId)
        val users = participations.mapNotNull { participation ->
            userService.findById(participation.userId)
        }

        return users
    }

    // Borrar una participación
    fun deleteParticipation(participationId: String): Boolean {
        return participationRepository.deleteById(participationId)
    }
}

