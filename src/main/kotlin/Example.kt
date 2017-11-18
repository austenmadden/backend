import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.dao.*

object Examples : IntIdTable() {
  val name = varchar("name", 50).index()
}

class Example(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Example>(Examples)

    var name by Examples.name
}

data class SimpleExample(val id: Int, val name: String)