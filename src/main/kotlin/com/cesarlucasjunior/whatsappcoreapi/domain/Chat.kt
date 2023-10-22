package com.cesarlucasjunior.whatsappcoreapi.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@Entity
@Table(name = "Chats")
data class Chat(
    @Id
    val id: String? = "5561999125142",
    val createdAt: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm")),
    val menuOption: String? = ""
) {

    fun isWithin24hours(createdAt: String): Boolean {
        val createdAtDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        val now = LocalDateTime.now()
        val twentyFourHoursAgo = now.minus(24, ChronoUnit.HOURS)

        return createdAtDateTime in twentyFourHoursAgo..now
    }
}


fun isWithin24hours(createdAt: String): Boolean {
    val createdAtDateTime = LocalDateTime.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    val now = LocalDateTime.now()
    val twentyFourHoursAgo = now.minus(24, ChronoUnit.HOURS)

    println("Data de criação - $createdAtDateTime")
    println("Data de agora - $now")
    println("Data de 24 horas atrás - $twentyFourHoursAgo")

    return createdAtDateTime in twentyFourHoursAgo..now
}
fun main() {
    println("${isWithin24hours("2023-10-19 23:12")}")
}
