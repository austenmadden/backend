import io.javalin.ApiBuilder.*
import io.javalin.Javalin
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.dao.*

fun main(args: Array<String>) {
  Database.connect(
    url = "jdbc:postgresql://localhost:5432/backend",
    driver = "org.postgresql.Driver",
    user = "amadden",
    password = "")

  transaction {
    create (Examples)
  }

  val app = Javalin.create().apply {
    enableStandardRequestLogging()
    port(7000)
  }.start()

  app.routes {
    get("/") { ctx -> ctx.status(404).json("Not Found") }
    path("api/examples/") {
      get(ApiController::listExamples);
      post(ApiController::createExample);
      path(":id") {
        /*get(ApiController::getExample);
        patch(ApiController::updateExample);
        delete(ApiController::deleteExample);*/
      }
    }
  }
}
