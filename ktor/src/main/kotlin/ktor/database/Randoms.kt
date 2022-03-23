package ktor.database

import org.jetbrains.exposed.dao.IntIdTable

object Randoms : IntIdTable() {
    val value = uuid("value").autoGenerate()
}
