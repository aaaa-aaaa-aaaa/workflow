package com.tray.workflow.app

import javax.inject.Inject

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finatra.http.exceptions.ExceptionMapper
import com.twitter.finatra.http.response.ResponseBuilder
import org.json4s.ParserUtil.ParseException

@Singleton
class ParseExceptionMapper @Inject()(response: ResponseBuilder)
    extends ExceptionMapper[ParseException] {

    override def toResponse(request: Request, exception: ParseException): Response = {
        response.badRequest(s"Body could not be parsed to JSON - ${exception.getMessage}")
    }
}
