package com.github.pshirshov.izumi.workshop.w01.plugins

import com.github.pshirshov.izumi.distage.plugins.PluginDef
import com.github.pshirshov.izumi.workshop.w01._
import scalaz.zio.IO


class BifunctorPlugin extends PluginDef {
  make[Bifunctor[IO]].from(Bifunctor.BifunctorZio)
  make[BiRunnable[IO]].from(BiRunnable.ZIORunner)
}
