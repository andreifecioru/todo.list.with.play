package models

import javax.inject.Inject

import slick.driver.JdbcProfile

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}


sealed trait TaskStatus
case object TaskActive extends TaskStatus
case object TaskCompleted extends TaskStatus


final case class Task(action: String,
                      status: TaskStatus,
                      id: TaskTablePK = TaskTablePK(0L))


final class TaskDAO @Inject()(val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {
  import driver.api._

  implicit val ts = MappedColumnType.base[TaskStatus, String](
    {
      case TaskActive => "active"
      case TaskCompleted => "completed"
    },
    {
      case "active" => TaskActive
      case "completed" => TaskCompleted
    }
  )

  final class TaskTable(tag: Tag) extends Table[Task](tag, "tasks"){
    def id = column[TaskTablePK]("id", O.PrimaryKey, O.AutoInc)
    def action = column[String]("action")
    def status = column[TaskStatus]("status")

    def * = (action, status, id) <> (Task.tupled, Task.unapply)
  }

  lazy val tasks = TableQuery[TaskTable]
  private lazy val insertTask = tasks returning tasks.map(_.id)

  def allTasks = db.run {
    tasks.sortBy(_.id).result
  }

  def activeTasks = db.run {
    tasks.filter(_.status === (TaskActive: TaskStatus)).sortBy(_.id).result
  }

  def completedTasks = db.run {
    tasks.filter(_.status === (TaskCompleted: TaskStatus)).sortBy(_.id).result
  }

  def create(task: Task) = db.run {
    insertTask into { (task, id) => task.copy(id = id) } += task
  }

  def update(task: Task) = db.run {
    tasks.filter(_.id === task.id).update(task)
      .map(count => if (count == 1) Some(task) else None)
  }

  def delete(task: Task) = db.run {
    tasks.filter(_.id === task.id).delete
  }

  def deleteAll = db.run { tasks.delete }
}