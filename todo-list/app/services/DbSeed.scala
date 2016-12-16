package services

import javax.inject._

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.{Configuration, Logger}
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future
import scala.util.{Failure, Success}

import models.{TaskCompleted, TaskActive, Task, TaskDAO}


@Singleton
class DbSeed @Inject() (
  taskDAO: TaskDAO,
  configuration: Configuration,
  appLifecycle: ApplicationLifecycle) {

  val envType = configuration.getString("environment.type")

  if (envType.contains("dev")) {
    Logger.info("Seeding the DB")

    val tasks =  Set(
      Task("Buy milk", TaskActive),
      Task("Do homework", TaskActive),
      Task("Clean the house", TaskCompleted)
    )

    taskDAO.deleteAll.foreach { count =>
      Logger.info(s"Resetting the tasks table (deleted $count entries")

      Future.sequence(tasks.map { task =>
        Logger.info(s"Adding task: $task")
        taskDAO.create(task)
      }).onComplete {
        case Success(_tasks) => Logger.info(s"Added ${_tasks.size} tasks to the DB")
        case Failure(e) => Logger.error(s"Failed to seed the DB: ${e.getMessage}")
      }
    }
  }

  appLifecycle.addStopHook { () =>
    Logger.info("Clearing all tasks")
    taskDAO.deleteAll
  }
}
