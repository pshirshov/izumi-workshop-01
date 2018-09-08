name := "distage-workshop"

version := "0.1"

scalaVersion := "2.12.6"

organization in ThisBuild := "com.github.pshirshov.izumi.workshop.w01"


enablePlugins(IzumiGitEnvironmentPlugin)


lazy val inRoot = In(".")

lazy val inRoles = In("role")

lazy val inApp = In("app")


lazy val usersRole = inRoles.as.module

lazy val accountsRole = inRoles.as.module

lazy val launcher = inApp.as.module
  .depends(usersRole, accountsRole)

lazy val workshop = inRoot.as.root
  .transitiveAggregate(launcher)
