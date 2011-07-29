package code
package snippet

import code.snippet.Param._
import net.liftweb.common.Logger

import  com.fmpwizard.cometactor.pertab.namedactor.InsertNamedComet

/**
  * This object adds a ComeActor of type BrowserDetails with a name == version it displays
  * This allows having multiple tabs open displaying different version results
  */
object PutCometOnPage extends Logger with InsertNamedComet {
  override lazy val cometClass= "BrowserDetails"
  override lazy val name= versionString
  info("Using CometActor with name: %s".format(name))
}
