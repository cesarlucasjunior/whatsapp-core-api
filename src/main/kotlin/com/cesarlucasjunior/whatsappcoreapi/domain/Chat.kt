package com.cesarlucasjunior.whatsappcoreapi.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.datetime.*
import java.sql.Time
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

@Entity
@Table(name = "Chats")
data class Chat(
    @Id
    val id: String? = "5561999125142",
    val createdAt: String = Clock.System.now().toLocalDateTime(TimeZone.of("America/Sao_Paulo")).toString(),
    val menuOption: String? = ""
) {

    fun isWithin24hours(createdAt: String): Boolean {
        val now = java.time.Instant.now()
        val createdAtInstant = java.time.Instant.parse(createdAt)
        val twentyFourHoursAgo = now.minus(1, ChronoUnit.HOURS)
        return createdAtInstant in twentyFourHoursAgo..now
    }
}


fun isWithin24hours(createdAt: String): Boolean {
    val now = java.time.Instant.now()
    val createdAtInstant = java.time.Instant.parse(createdAt)
    val twentyFourHoursAgo = now.minus(1, ChronoUnit.HOURS)
    println("Created At comparator - $createdAtInstant")
    return createdAtInstant in twentyFourHoursAgo..now
}

 fun main() {
     val createdAt = java.time.LocalDateTime.of(2023, Month.OCTOBER, 19, 22, 29)
     println("Data de criação - $createdAt")
     println("Está dentro de 24h - ${isWithin24hours(createdAt.atZone(ZoneId.of("America/Sao_Paulo")).toInstant().toString())}")
 }
