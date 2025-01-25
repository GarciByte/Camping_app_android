package com.david.servidor.repository

import com.david.servidor.model.Activity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ActivityRepository : CrudRepository<Activity, String> {

    // Obtener actividades por nombre
    fun findByName(name: String): List<Activity>

    // Obtener actividades de un lugar específico
    fun findByPlace(place: String): List<Activity>

}
