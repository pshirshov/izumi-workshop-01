package com.github.pshirshov.izumi.workshop.w01.plugins

import com.github.pshirshov.izumi.distage.plugins.PluginDef
import com.github.pshirshov.izumi.workshop.w01.AccountsRole
import scalaz.zio.IO

class AccountsPlugin extends PluginDef {
  make[AccountsRole[IO]]
}
