package com.github.pshirshov.izumi.workshop.w01

import Bifunctor._

case class UserId(id: String) extends AnyVal

case class Email(email: String) extends AnyVal

case class PersonName(name: String, surname: String)

case class User(id: UserId, name: PersonName, email: Email)

trait UserStorage[F[_, _]] {
  def saveUser(user: User): F[Nothing, Unit]

  def findByEmail(email: Email): F[Nothing, List[User]]
}

class DummyUserStorage[F[+_, +_] : Bifunctor]
(
  storage: AbstractStorage[F, UserId, User]
) extends UserStorage[F] {

  override def saveUser(user: User): F[Nothing, Unit] = storage.store(user.id, user)

  override def findByEmail(email: Email): F[Nothing, List[User]] = storage.enumerate()
    .map(_.filter(_.email == email))
}
