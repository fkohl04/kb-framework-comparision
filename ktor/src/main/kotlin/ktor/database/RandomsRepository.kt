package ktor.database

import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class RandomsRepository {
    fun findAllValues(): List<String> {
        val list: ArrayList<String> = arrayListOf()
        transaction {
            (Randoms).selectAll()
                .forEach {
                    list.add(it[Randoms.value].toString())
                }
        }
        return list
    }
}
