package se.ramn.models.v2

import java.util.UUID
import java.nio.file.Path

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

import se.ramn.Config


case class Bot(
  id: UUID = UUID.randomUUID,
  name: String,
  ownerId: UUID,
  createdAt: DateTime = DateTime.now(DateTimeZone.UTC),
  modifiedAt: DateTime = DateTime.now(DateTimeZone.UTC)
)


case class BotVersion(
  id: UUID = UUID.randomUUID,
  botId: UUID,
  originalFilename: String,
  contentType: String,
  sizeBytes: Long,
  createdAt: DateTime = DateTime.now(DateTimeZone.UTC),
  modifiedAt: DateTime = DateTime.now(DateTimeZone.UTC)
) {
  def persistedFilename: String = id.toString + ".jar"
  def persistedPath: Path = Config.BotDir.toPath.resolve(persistedFilename)
  def bot: Bot = {
    BotRepository.get(botId).get
  }
}
