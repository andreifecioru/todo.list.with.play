package controllers

import javax.inject._
import models.TaskDAO
import play.api._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class HomeController @Inject()(
  taskDAO: TaskDAO,
  val messagesApi: MessagesApi,
  implicit val config: Configuration
) extends Controller with I18nSupport {

  def index = Action.async {
    Future.sequence(Seq(
      taskDAO.activeTasks,
      taskDAO.completedTasks
    )).map { tasks =>
      val activeTasks = tasks.head
      val completedTasks = tasks.last
      Ok(views.html.index(activeTasks, completedTasks))
    }
  }
}
