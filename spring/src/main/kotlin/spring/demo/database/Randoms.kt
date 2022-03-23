package spring.demo.database

import java.util.UUID
import javax.annotation.Generated
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "randoms")
class Randoms {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
    var value: UUID? = null
}
