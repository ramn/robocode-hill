package models

import java.io.File
//import collection.concurrent
//import collection.JavaConverters._

import org.mapdb.DBMaker
import org.mapdb.DB
import org.mapdb.TxMaker
import org.mapdb.Fun


class DbUtil(dbMaker: DBMaker) {
  private val txMaker = dbMaker.makeTxMaker
  val db = dbMaker.make

  def makeTx = txMaker.makeTx

  def inTx[T](thunk: DB => T): T = {
    val fn = new Fun.Function1[T, DB] {
      def run(db: DB): T = {
        thunk(db)
      }
    }
    txMaker.execute(fn)
  }
}


object DbUtil {
  def newFileDB(dbFile: File): DbUtil =
    makeDbUtil(DBMaker.newFileDB(dbFile).mmapFileEnableIfSupported)

  def newMemoryDB = makeDbUtil(DBMaker.newMemoryDB)

  private def makeDbUtil(initialMaker: DBMaker): DbUtil = {
    val dbMaker = initialMaker
    //.asyncWriteEnable
    .cacheLRUEnable
    .checksumEnable
    .closeOnJvmShutdown
    //.snapshotEnable
    new DbUtil(dbMaker)
  }
}
