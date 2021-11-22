package lectures.session5.part4_database

import doobie.Meta

import java.time.Instant
import java.util.UUID

private[part4_database] trait DatabaseBoilerplate {
  implicit val instantMeta: Meta[Instant] = doobie.postgres.implicits.JavaTimeInstantMeta

  implicit val uuidMeta: Meta[UUID] = doobie.postgres.implicits.UuidType
}
