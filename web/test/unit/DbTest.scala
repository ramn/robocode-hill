package unit

import java.io.File
import java.util.UUID
import collection.concurrent
//import collection.mutable
//import collection.JavaConversions._
import collection.JavaConverters._

import org.scalatest.FunSuite
import org.scalatest.FunSpec

import models.Bot
import models.DbUtil


class DbTest extends FunSpec {
  describe("an in memory db") {
    describe("when using transactions") {
      it("should conform to ACID") {
        val dbUtil = DbUtil.newMemoryDB

        val botId = UUID.fromString("64f264c8-170d-4c44-b742-9f6b4d8b8726")

        val tx1 = dbUtil.makeTx

        val expectedBot = Bot(jar=new File("activator"))

        dbUtil inTx { db =>
          val botsTable: concurrent.Map[UUID, Bot] = db.getTreeMap[UUID, Bot]("bots").asScala
          botsTable.put(botId, Bot(jar=new File("activator")))
        }

        val fetchedBotOpt: Option[Bot] = dbUtil.inTx { db =>
          val botsTable: concurrent.Map[UUID, Bot] = db.getTreeMap[UUID, Bot]("bots").asScala
          val bot = botsTable.get(botId)
          assert(bot === Some(expectedBot))
          bot
        }

        {
          val table = tx1.getTreeMap[UUID, Bot]("bots").asScala
          assert(table.get(botId) === None,
            "bot should not be visible in tx1 which was opened before bot was created")
        }

        val tx2 = dbUtil.makeTx
        tx1.close()

        {
          val table: concurrent.Map[UUID, Bot] = tx2.getTreeMap[UUID, Bot]("bots").asScala
          assert(table.get(botId) === Some(expectedBot))
          tx2.close()
        }
      }
    }
  }
}
