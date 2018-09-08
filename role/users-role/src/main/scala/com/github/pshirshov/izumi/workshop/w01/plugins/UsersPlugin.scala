package com.github.pshirshov.izumi.workshop.w01.plugins

import com.github.pshirshov.izumi.distage.plugins.PluginDef
import com.github.pshirshov.izumi.workshop.w01.UsersRole
import scalaz.zio.IO

class UsersPlugin extends PluginDef  {
  make[UsersRole[IO]]
}
