package com.david.servidor.repository

import com.david.servidor.model.Participation
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*

@Repository
class ParticipationRepository(private val db: JdbcTemplate) {

    // Registrar una nueva participación
    fun save(participation: Participation): Participation? {

        if (existingUserAndActivity(participation.userId, participation.activityId.toString())) {
            return null
        }

        val id = UUID.randomUUID().toString()
        db.update(
            "INSERT INTO PARTICIPATIONS (id, user_id, activity_id) VALUES (?, ?, ?)",
            id, participation.userId, participation.activityId
        )
        return participation.copy(id = id)
    }

    // Verificar si el usuario ya ha participado en esa actividad
    fun existingUserAndActivity(userId: String, activityId: String): Boolean {
        val participation = db.queryForRowSet(
            "SELECT COUNT(*) AS count FROM PARTICIPATIONS WHERE user_id = ? AND activity_id = ?",
            userId, activityId
        )

        return participation.next() && participation.getInt("count") > 0
    }

    // Obtener todas las participaciones
    fun findAll(): List<Participation> {
        return db.query("SELECT * FROM PARTICIPATIONS") { response, _ -> participationMap(response) }
    }

    // Obtener todas las participaciones de un usuario
    fun findByUserId(userId: String): List<Participation> {
        return db.query("SELECT * FROM PARTICIPATIONS WHERE user_id = ?", { response, _ -> participationMap(response) }, userId)
    }

    // Obtener todas las participaciones de una actividad
    fun findByActivityId(activityId: String): List<Participation> {
        return db.query("SELECT * FROM PARTICIPATIONS WHERE activity_id = ?", { response, _ -> participationMap(response) }, activityId)
    }

    // Obtener una participación por su ID
    fun findById(id: String): Participation? {
        return try {
            db.queryForObject(
                "SELECT * FROM PARTICIPATIONS WHERE id = ?",
                { response, _ -> participationMap(response) },
                id
            )
        } catch (e: EmptyResultDataAccessException) {
            print("No existe esa participación")
            null
        }
    }

    // Borrar participación
    fun deleteById(id: String): Boolean {
        val existingParticipation = findById(id)
        if (existingParticipation == null) {
            return false
        } else {
            db.update("DELETE FROM PARTICIPATIONS WHERE id = ?", id)
            return true
        }
    }

    private fun participationMap(rs: ResultSet): Participation {
        return Participation(
            id = rs.getString("id"),
            userId = rs.getString("user_id"),
            activityId = rs.getLong("activity_id")
        )
    }

}
