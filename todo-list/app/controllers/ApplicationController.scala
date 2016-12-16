package controllers

import play.api.mvc.{Action, Controller}
import play.api.routing.JavaScriptReverseRouter

class ApplicationController extends Controller {

  def jsRoutes = Action { implicit request =>
    Ok(
      JavaScriptReverseRouter("jsRoutes")(
      )
    ).as("text/javascript")
  }

}
