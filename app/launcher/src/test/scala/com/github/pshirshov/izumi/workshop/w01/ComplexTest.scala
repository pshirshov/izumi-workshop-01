package com.github.pshirshov.izumi.workshop.w01

import com.github.pshirshov.izumi.distage.planning.gc.TracingGcModule
import com.github.pshirshov.izumi.distage.plugins.load.PluginLoaderDefaultImpl
import com.github.pshirshov.izumi.distage.plugins.load.PluginLoaderDefaultImpl.PluginConfig
import com.github.pshirshov.izumi.distage.plugins.merge.ConfigurablePluginMergeStrategy
import com.github.pshirshov.izumi.distage.plugins.merge.ConfigurablePluginMergeStrategy.PluginMergeConfig
import com.github.pshirshov.izumi.fundamentals.tags.TagExpr
import com.github.pshirshov.izumi.logstage.api.{IzLogger, Log}
import com.github.pshirshov.izumi.logstage.distage.LogstageModule
import com.github.pshirshov.izumi.logstage.sink.ConsoleSink
import distage._
import org.scalatest.WordSpec
import scalaz.zio.IO

class ComplexTest extends WordSpec {
  def dit[T1: Tag, T2: Tag](f: (T1, T2) => Unit): Unit = {
    di((a: (T1, T2)) => f(a._1, a._2))
  }

  def di[T: Tag](f: T => Unit): Unit = {
    val modules = new PluginLoaderDefaultImpl(
      PluginConfig(debug = false, Seq("com.github.pshirshov.izumi.workshop.w01"), Seq.empty)
    ).load()

    val mergeStrategy = new ConfigurablePluginMergeStrategy(PluginMergeConfig(
      TagExpr.Strings.any("dummy")
      , Set.empty
      , Set.empty
      , Map.empty
    ))

    val primaryModule = mergeStrategy.merge(modules).definition

    val injector = Injector
      .bootstrap(overrides = Seq[BootstrapModule](new TracingGcModule(Set(DIKey.get[T]))).merge)

    val plan = injector.plan(Seq(primaryModule, new LogstageModule(IzLogger.simpleRouter(Log.Level.Debug, ConsoleSink.ColoredConsoleSink)), new ModuleDef {
      make[T]
    }).merge)

    val context = injector.produce(plan)
    val fixture = context.get[T]
    f(fixture)
  }

  "DI test" must {
    "inject fixtures" in di {
      fixture: UsersRole[IO] =>
        assert(fixture.isInstanceOf[UsersRole[IO]])
        println(s"Fixture: $fixture")
    }

    "inject tuples" in dit {
      (fixture: UsersRole[IO], logger: IzLogger) =>
        assert(fixture.isInstanceOf[UsersRole[IO]])
        logger.info(s"Fixture: $fixture")
    }
  }
}
