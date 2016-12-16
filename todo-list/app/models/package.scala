package object models {
  import slick.lifted.MappedTo

  final case class TaskTablePK(value: Long) extends AnyVal with MappedTo[Long]
}
