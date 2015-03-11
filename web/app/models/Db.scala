package se.ramn
package models

import java.io.File
import org.mapdb.DB


object Db extends DbAccessor {
  private val dbAccessor = DbAccessor.newFileDB(Config.DbDir)

  override def makeTx: DB = dbAccessor.makeTx
  override def inTx[T](thunk: DB => T): T = dbAccessor.inTx(thunk)
}
