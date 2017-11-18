import io.javalin.Context
import java.util.*
import java.sql.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.exposed.dao.EntityID


data class Post(val name: String = "")

object ApiController {
  fun createExample(ctx: Context) {
    connectDatabase()

    try {
      val newExamplePost = ctx.bodyAsClass(Post::class.java)
      transaction {
        println(newExamplePost.name)
        val newExample = Examples.insert {
          it[name] = newExamplePost.name
        }
        ctx.status(201)
      }
    } catch (e: BatchUpdateException) {
      println(e.getNextException())
      ctx.status(500)
    }
  }

  fun listExamples(ctx: Context) {
    connectDatabase()
    transaction {
      val exampleList = mutableListOf<SimpleExample>()
      for (example in Examples.selectAll()) {
        exampleList.add(SimpleExample(example[Examples.id].value, example[Examples.name]))
      }
      val json = ObjectMapper().writeValueAsString(exampleList)
      ctx.status(200).json(json)
    }
  }

  private fun connectDatabase() {
    Database.connect(
      url = "jdbc:postgresql://localhost:5432/backend",
      driver = "org.postgresql.Driver",
      user = "amadden",
      password = "")
  }
}
