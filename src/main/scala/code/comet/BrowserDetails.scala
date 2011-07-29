package code
package comet

import code.api.CellToUpdate
import code.lib.GridRenderHelper._
import code.model.BrowserTests
import code.snippet.Param._


import scala.xml.{NodeSeq, Text, Elem}

import net.liftweb._
import util._
import actor._
import http._
import common.{Box, Full,Logger}
import http.js.JsCmds.Replace
import Helpers._


import com.fmpwizard.cometactor.pertab.namedactor._




class BrowserDetails extends  SimpleInjector with Logger with NamedCometActor {

  override def defaultPrefix = Full("comet")

  // time out the comet actor if it hasn't been on a page for 2 miniutes
  override def lifespan = Full(120 seconds)

  var showingVersion= versionString

  val testResults= new Inject[Box[List[Map[String,(String, String)]]]](
    Full(List(Map("N/A"->("N/A", "N/A"))))
  ){}
  testResults.default.set(getTestResults)


  // Inject the method call, not just the val
  def getTestResults: Box[List[Map[String,(String, String)]]]= {
    val testResultControlRender= BrowserTests.getBrowserTestResultByBrowserName(
      showingVersion, "Control render" )
    val testResultGraphsRender= BrowserTests.getBrowserTestResultByBrowserName(
      showingVersion, "Graphs render" )
    val testResultCGWorks= BrowserTests.getBrowserTestResultByBrowserName(
      showingVersion, "CG Works" )
    val testResultNoErrors= BrowserTests.getBrowserTestResultByBrowserName(
      showingVersion, "No Errors" )

    // a list of all our tests
    val tests = Full(List(testResultControlRender, testResultGraphsRender,
      testResultCGWorks, testResultNoErrors))
    tests

  }

  /**
   * On page load, this method does a full page render
   */
  def render= {
    testResults.doWith(getTestResults){
      renderGrid(showingVersion, testResults.vend)
    }
  }

  /**
   * We can get two kinds of messages
   * 1- A CellToUpdate, which has info about which cell on the UI
   * we need to update. The rest api sends this message
   *
   * 2- A string which is the version the comet actor is displaying info about
   * On page load we get this message
   *
   */

  override def lowPriority: PartialFunction[Any,Unit] = {
    case CellToUpdate(index, rowName, version, cssClass, cellNotes) => {
      info("Comet Actor %s will do a partial update".format(this))
      info("[API]: Updating BrowserTestResults for version: %s".format(version))
      showingVersion = version

      /**
       * each td in the html grid has an id that is
       * [0-9] + browser name
       * I use this to uniquely identify which cell to update
       *
       */
      partialUpdate(
        Replace((index + rowName),
            <td id={(index + rowName)} class={cssClass}>{cellNotes}</td>
         )
      )
    }
  }



}
