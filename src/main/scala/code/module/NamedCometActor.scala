package com.fmpwizard.cometactor.pertab
package namedactor

import net.liftweb.common.Logger
import net.liftweb.http.CometActor
import net.liftweb.common.Full

/**
 * Created by IntelliJ IDEA.
 * User: Diego Medina
 * Date: 7/26/11
 * Time: 2:27 PM
 */




trait NamedCometActor extends CometActor with Logger{


  /**
   * Because we are using localSetup(), we do not need these few lines
    */
  //override  def composeFunction: PartialFunction[Any, Unit] = setupFunc orElse super.composeFunction
  /*def setupFunc: PartialFunction[Any, Unit] = {
    case CometName(name) => {
      info("[URL]: Updating BrowserTestResults for version: %s".format(name))
      //showingVersion= version
      /**
       * We get the DispatcherActor that sends message to all the
       * CometActors that are displaying a specific version number.
       * And we register ourselves with the dispatcher
       */
      CometListerner.listenerFor(Full(name)) ! registerCometActor(this, Full(name))
      info("Registering comet actor: %s".format(this))
      reRender()
    }
  }*/

  override  def localSetup = {
    CometListerner.listenerFor(name) ! registerCometActor(this, name)
    info("====>>>>>>>>>> %s".format(name))
    super.localSetup()
  }

  override  def localShutdown = {
    CometListerner.listenerFor(name) ! unregisterCometActor(this)
    super.localShutdown()
  }




}
