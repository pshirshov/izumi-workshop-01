package com.github.pshirshov.izumi.workshop.w01

import BifunctorIO._

import scala.collection.mutable

case class UserId(id: String) extends AnyVal

case class Email(email: String) extends AnyVal

case class PersonName(name: String, surname: String)

case class User(id: UserId, name: PersonName, email: Email)

trait UserStorage[F[_, _]] {
  def saveUser(user: User): F[Nothing, Unit]

  def findByEmail(email: Email): F[Nothing, List[User]]
}

class DummyUserStorage[F[+ _, + _] : BifunctorIO]
(
  storage: AbstractStorage[F, UserId, User]
) extends UserStorage[F] {

  override def saveUser(user: User): F[Nothing, Unit] = storage.store(user.id, user)

  override def findByEmail(email: Email): F[Nothing, List[User]] = storage.enumerate()
    .map(_.filter(_.email == email))
}


case class UserAccount(balance: BigDecimal)

trait AccountStorage[F[_, _]] {
  // We have a race here, not a production code!
  def fetch(userId: UserId): F[StorageError, UserAccount]

  def save(userId: UserId, account: UserAccount): F[Nothing, Unit]
}

class DummyAccountStorage[F[+ _, + _] : BifunctorIO]
(
  storage: AbstractStorage[F, UserId, UserAccount]
) extends AccountStorage[F] {

  override def fetch(userId: UserId): F[StorageError, UserAccount] = storage.fetch(userId)

  override def save(userId: UserId, account: UserAccount): F[Nothing, Unit] = storage.store(userId, account)

}

sealed trait AccountingError

object AccountingError {

  case object NotFound extends AccountingError

  case object NegativeBalance extends AccountingError

  case object UserAlreadyExists extends AccountingError

}


trait AccountingService[F[_, _]] {
  def getBalance(userId: UserId): F[AccountingError, BigDecimal]

  def updateBalance(userId: UserId, delta: BigDecimal): F[AccountingError, BigDecimal]

  def createAccount(userId: UserId): F[AccountingError, Unit]
}

class TrivialAccountingImpl[F[+ _, + _] : BifunctorIO] extends AccountingService[F] {

  import BifunctorIO._

  val balances = mutable.HashMap[UserId, BigDecimal]()

  override def getBalance(userId: UserId): F[AccountingError, BigDecimal] = {
    BifunctorIO[F].fromEither {
      balances.synchronized {
        balances.get(userId).toRight(AccountingError.NotFound)
      }
    }
  }

  override def updateBalance(userId: UserId, delta: BigDecimal): F[AccountingError, BigDecimal] = {
    balances.synchronized {
      balances.get(userId) match {
        case Some(value) =>
          val newBalance = value + delta
          if (newBalance >= 0) {
            balances.put(userId, newBalance)
            BifunctorIO[F].point(newBalance)
          } else {
            BifunctorIO[F].fail(AccountingError.NegativeBalance)
          }
        case None =>
          BifunctorIO[F].fail(AccountingError.NotFound)
      }
    }
  }

  override def createAccount(userId: UserId): F[AccountingError, Unit] = {
    balances.synchronized {
      if (balances.contains(userId)) {
        BifunctorIO[F].fail(AccountingError.UserAlreadyExists)
      } else {
        BifunctorIO[F].point {
          balances.put(userId, BigDecimal(0))
          ()
        }
      }
    }
  }
}
