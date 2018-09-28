import com.github.pshirshov.izumi.sbt.deps.IzumiDeps
import com.github.pshirshov.izumi.sbt.deps.IzumiDeps.{R, V}
import sbt.Keys.libraryDependencies

name := "distage-workshop"

version := "0.1"

scalaVersion := "2.12.6"

organization in ThisBuild := "com.github.pshirshov.izumi.workshop.w01"


enablePlugins(IzumiGitEnvironmentPlugin)

val GlobalSettings = new DefaultGlobalSettingsGroup {
  override val settings: Seq[sbt.Setting[_]] = Seq(
    crossScalaVersions := Seq(
      V.scala_212,
    ),
    addCompilerPlugin(R.kind_projector),
    libraryDependencies ++= Seq(Izumi.R.distage_plugins, IzumiDeps.T.scalatest)
      ++ IzumiDeps.R.cats_all ++ IzumiDeps.R.zio,
  )
}

lazy val WithoutBadPlugins = new SettingsGroup {
  override val disabledPlugins: Set[sbt.AutoPlugin] = Set(AssemblyPlugin)
}

lazy val AppSettings = new SettingsGroup {
  override val plugins: Set[sbt.Plugins] = Set(AssemblyPlugin)
  override val settings: Seq[sbt.Setting[_]] = Seq(
    libraryDependencies ++= Seq(Izumi.R.distage_roles),
  )
}

lazy val RoleSettings = new SettingsGroup {
  override val settings: Seq[sbt.Setting[_]] = Seq(
    libraryDependencies ++= Seq(Izumi.R.distage_roles_api, Izumi.R.logstage_api_logger),
  )
}

val SbtSettings = new SettingsGroup {
  override val settings: Seq[sbt.Setting[_]] = Seq(
    Seq(
      target ~= { t => t.toPath.resolve("primary").toFile }
      , crossScalaVersions := Seq(
        V.scala_212
      )
      , libraryDependencies ++= Seq(
        "org.scala-sbt" % "sbt" % sbtVersion.value
      )
      , sbtPlugin := true
    )
  ).flatten
}

lazy val inRoot = In(".").settings(WithoutBadPlugins)
lazy val inLib = In("lib").settings(GlobalSettings, WithoutBadPlugins)

lazy val inRoles = In("role").settings(GlobalSettings, RoleSettings, WithoutBadPlugins)

lazy val inApp = In("app").settings(GlobalSettings, AppSettings)

lazy val inSbt = In("sbt").settings(SbtSettings, WithoutBadPlugins)

lazy val common = inLib.as.module

lazy val usersRole = inRoles.as.module
  .depends(common)

lazy val accountsRole = inRoles.as.module
  .depends(common)

lazy val launcher = inApp.as.module
  .depends(usersRole, accountsRole)

lazy val sbtBomWorkshop = inSbt.as
  .module
  .settings(withBuildInfo("com.github.pshirshov.izumi.workshop.sbt.deps", "Workshop"))

lazy val workshop = inRoot.as.root
  .transitiveAggregate(launcher, sbtBomWorkshop)


/*
At this point use thse commands to setup project layout from sbt shell:

newModule role/accounts-role
newModule role/users-role
newModule app/launcher
*/
