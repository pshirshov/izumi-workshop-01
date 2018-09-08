package com.github.pshirshov.izumi.workshop.w01.plugins

import com.github.pshirshov.izumi.distage.plugins.PluginDef
import com.github.pshirshov.izumi.workshop.w01.UsersRole

class UsersPlugin extends PluginDef  {
  make[UsersRole]
}
