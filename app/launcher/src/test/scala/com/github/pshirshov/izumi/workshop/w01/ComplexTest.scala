package com.github.pshirshov.izumi.workshop.w01

import com.github.pshirshov.izumi.distage.model.definition.Binding.SingletonBinding
import com.github.pshirshov.izumi.distage.model.definition.{ImplDef, Module}
import com.github.pshirshov.izumi.distage.model.providers.ProviderMagnet
import com.github.pshirshov.izumi.distage.model.reflection.universe.RuntimeDIUniverse
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
  def di[T: Tag](f: T => Any): Unit = {
    val providerMagnet: ProviderMagnet[Unit] = { x: T => f(x); () }
    di(providerMagnet)
  }

  def di(f: ProviderMagnet[Unit]): Unit = {
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

    val roots: Set[RuntimeDIUniverse.DIKey] = f.get.diKeys.toSet

    val injector = Injector
      .bootstrap(overrides = Seq[BootstrapModule](new TracingGcModule(roots)).merge)

    val fixtureModule = Module.make(roots.map {
      key => SingletonBinding(key, ImplDef.TypeImpl(key.tpe))
    })
    val plan = injector.plan(
      Seq(fixtureModule
        , primaryModule
        , new LogstageModule(IzLogger.simpleRouter(Log.Level.Debug, ConsoleSink.ColoredConsoleSink))
      ).overrideLeft
    )

    val context = injector.produce(plan)

    context.instances.foreach {
      i =>
        println(i)
    }

    context.run(f)
  }

  "DI test" must {
    "inject fixtures" in di {
      fixture: UsersRole[IO] =>
        assert(fixture.isInstanceOf[UsersRole[IO]])
        println(s"Fixture: $fixture")
    }

    "inject tuples" in di [(AccountsRole[IO], IzLogger)]  {
      case (fixture: AccountsRole[IO], logger: IzLogger) =>
        assert(fixture.isInstanceOf[AccountsRole[IO]])
        logger.info(s"Fixture: $fixture")
    }

    "inject multiple arguments" in di {
      (fixture: AccountsRole[IO], logger: IzLogger) =>
        assert(fixture.isInstanceOf[AccountsRole[IO]])
        logger.info(s"Fixture: $fixture")
    }

    "inject abstract arguments" in di {
      storage: UserStorage[IO] =>
        assert(storage.isInstanceOf[DummyUserStorage[IO]])
    }
  }
}
