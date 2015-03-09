package models

import java.io.File
import java.util.UUID
import collection.mutable
import collection.JavaConversions._
import scala.language.implicitConversions

import org.mapdb.DBMaker
import org.mapdb.DB
import org.mapdb.TxMaker
import org.mapdb.TxBlock


class BotRepository {
}


object BotRepository {
  //val cache: mutable.Map[String, Seq[String]] =
  //  DBMaker.newTempHashMap[String, Seq[String]]()
  //val db: collection.concurrent.Map[String, Seq[String]] =
    //DBMaker.newTempHashMap[String, Seq[String]]()

  val maker: DBMaker[_] = DBMaker
    //.newFileDB(new File("/tmp/my_temp_mapdb.db"))
    .newTempFileDB
    .asyncWriteEnable
    .cacheLRUEnable
    .checksumEnable
    .closeOnJvmShutdown
    .mmapFileEnableIfSupported
    .snapshotEnable

  //val db: DB = maker.make // supports only one transaction at a time

  val txMaker: TxMaker = maker.makeTxMaker
  //val tx1: DB = txMaker.makeTx
  //val tx2: DB = txMaker.makeTx

  val botId = UUID.fromString("64f264c8-170d-4c44-b742-9f6b4d8b8726")

  txMaker.execute { db: DB =>
    val botsTable: mutable.Map[UUID, Bot] = db.getTreeMap[UUID, Bot]("bots")
    botsTable.put(botId, Bot(jar=new File("activator")))
  }

  txMaker.execute { db: DB =>
    val botsTable: mutable.Map[UUID, Bot] = db.getTreeMap[UUID, Bot]("bots")
    val bot = botsTable.get(botId)
    println(bot)
    assert(bot == Some(Bot(jar=new File("activator"))))
  }


  implicit def inTx[T](thunk: DB => T): TxBlock = new TxBlock {
    def tx(db: DB): Unit = {
      thunk(db)
      ()
    }
  }
}
