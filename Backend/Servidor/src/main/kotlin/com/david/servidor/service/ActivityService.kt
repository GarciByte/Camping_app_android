package com.david.servidor.service

import com.david.servidor.model.Activity
import com.david.servidor.repository.ActivityRepository
import org.springframework.stereotype.Service

@Service
class ActivityService(private val activityRepository: ActivityRepository) {

    // Crear una actividad
    fun create(activity: Activity): Activity = activityRepository.save(activity)

    // Obtener todas las actividades
    fun findAll(): List<Activity> = activityRepository.findAll().toList()

    // Buscar una actividad por su ID
    fun findById(id: String): Activity? = activityRepository.findById(id).orElse(null)

    // Buscar actividades por nombre
    fun findByName(name: String): List<Activity> = activityRepository.findByName(name)

    // Buscar actividades por lugar
    fun findByPlace(place: String): List<Activity> = activityRepository.findByPlace(place)

    // Eliminar una actividad por su ID
    fun deleteById(id: String): Boolean {
        return if (activityRepository.existsById(id)) {
            activityRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}


