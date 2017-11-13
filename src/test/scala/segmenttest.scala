/**
  * Created by samlow on 13/11/17.
  */
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits.global
import scalaj.http.{Http, HttpOptions}

class SegmentTest extends FunSuite with ScalaFutures{

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1200, Seconds), interval = Span(5000, Millis))

  test("segment HTTP API") {
    implicit val baseTime = System.currentTimeMillis

    val SegmentURL = "https://api.segment.io/v1/track"

    def posttoSegment(SegmentURL: String) = Future{
      Http(SegmentURL).postData("""{"userId" : "usertestfromapi", "event" : "Answer Created", "properties" : {"questionId": "test-segment-1234", "rephrase": "{}", "confidenceLevel": "90", "result_time": "12:34:56"}}""")
        .header("Content-Type", "application/json").auth("8y5NzF68AZoR328fzBKop96Zl8lXJwWX", "")
        .option(HttpOptions.readTimeout(10000)).asString
    }

    try{
      println("**** going to post to segment..")
      val s = posttoSegment(SegmentURL)
      println("**** submitted the request..")
      println("**** waiting for result..")
      Await.ready(s, Duration.Inf).value.get match {
        case Success(result) => println(s"result = $result")
        case Failure(e) => e.printStackTrace
      }
      println("**** finished waiting")
      println("**** test ended")
    } catch {
      case a: Exception =>
        println(a)
      case t:Throwable =>
        println(t)
    }

  }
}

