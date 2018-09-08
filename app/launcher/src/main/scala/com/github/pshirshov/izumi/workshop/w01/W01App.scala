package com.github.pshirshov.izumi.workshop.w01

import com.github.pshirshov.izumi.distage.plugins.load.PluginLoaderDefaultImpl.PluginConfig
import com.github.pshirshov.izumi.distage.roles.impl.ScoptRoleApp
import com.github.pshirshov.izumi.distage.roles.launcher.{RoleApp, RoleAppBootstrapStrategy}

class W01App extends RoleApp with ScoptRoleApp {
  override val pluginConfig = PluginConfig(
    debug = false
    , packagesEnabled = Seq("com.github.pshirshov.izumi.workshop.w01.plugins")
    , packagesDisabled = Seq.empty
  )

  override protected def using: Seq[RoleAppBootstrapStrategy.Using] = Seq.empty
}


