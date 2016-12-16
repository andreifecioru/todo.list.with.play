import com.google.inject.AbstractModule
import java.time.Clock

import net.codingwell.scalaguice.ScalaModule

import services.DbSeed

class Module extends AbstractModule with ScalaModule {

  override def configure() = {
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)

    // Seed the DB @ application startup
    bind[DbSeed].asEagerSingleton()
  }

}
