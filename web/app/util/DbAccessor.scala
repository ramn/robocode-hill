package se.ramn.models

import java.io.File
//import collection.concurrent
//import collection.JavaConverters._

import org.mapdb.DBMaker
import org.mapdb.DB
import org.mapdb.TxMaker
import org.mapdb.Fun


trait DbAccessor {
  def makeTx: DB
  def inTx[T](thunk: DB => T): T
}


private class DbAccessorImpl(dbMaker: DBMaker) extends DbAccessor {
  private val txMaker = dbMaker.makeTxMaker
  private val db = dbMaker.make

  def makeTx: DB = txMaker.makeTx

  def inTx[T](thunk: DB => T): T = {
    val fn = new Fun.Function1[T, DB] {
      def run(db: DB): T = {
        thunk(db)
      }
    }
    txMaker.execute(fn)
  }
}


object DbAccessor {
  def newFileDB(dbFile: File): DbAccessor =
    makeDbUtil(DBMaker.newFileDB(dbFile).mmapFileEnableIfSupported)

  def newMemoryDB = makeDbUtil(DBMaker.newMemoryDB)

  private def makeDbUtil(initialMaker: DBMaker): DbAccessor = {
    val dbMaker = initialMaker
    //.asyncWriteEnable
    .cacheLRUEnable
    .checksumEnable
    .closeOnJvmShutdown
    //.snapshotEnable
    new DbAccessorImpl(dbMaker)
  }
}
