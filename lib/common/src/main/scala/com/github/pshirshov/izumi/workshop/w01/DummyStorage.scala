package com.github.pshirshov.izumi.workshop.w01

import scala.collection.mutable

sealed trait StorageError

object StorageError {

  case object NotFound extends StorageError

}

trait AbstractStorage[F[_, _], K, V] {
  def store(key: K, value: V): F[Nothing, Unit]

  def delete(key: K): F[StorageError, Unit]

  def fetch(key: K): F[StorageError, V]

  def enumerate(): F[Nothing, List[V]]
}

class DummyStorage[F[+_, +_] : Bifunctor, K, V] extends AbstractStorage[F, K, V] {
  protected val values = new mutable.HashMap[K, V]()

  override def store(key: K, value: V): F[Nothing, Unit] = Bifunctor[F].sync {
    values.synchronized {
      values.put(key, value)
    }
  }

  override def fetch(key: K): F[StorageError, V] = Bifunctor[F].fromEither {
    values.synchronized {
      values.get(key).toRight(StorageError.NotFound)
    }
  }

  override def delete(key: K): F[StorageError, Unit] = Bifunctor[F].fromEither {
    values.synchronized {
      values.remove(key).toRight(StorageError.NotFound).map(_ => ())
    }
  }

  override def enumerate(): F[Nothing, List[V]] = Bifunctor[F].sync {
    values.synchronized {
      values.values.toList
    }
  }

}




