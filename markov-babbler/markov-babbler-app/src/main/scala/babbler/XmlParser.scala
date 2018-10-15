package babbler

import scalaj.http.{Http, HttpResponse}
import scala.xml.XML

object XmlParser {

  // todo: for testing only, remove when done
  val link1: String = "http://www.refsmmat.com/rss.xml"
  val link2: String = "http://feeds.arstechnica.com/arstechnica/index"
  val link3: String = "http://feeds.feedburner.com/RBloggers?format=xml"

  def parse(link: String): String = {
    // get the xml content using scalaj-http
    val response: HttpResponse[String] = Http(link)
      .timeout(connTimeoutMs = 2000, readTimeoutMs = 5000)
      .asString
    val xmlString = response.body

    // convert the `String` to a `scala.xml.Elem`
    val xml = XML.loadString(xmlString)

    // handle the xml as desired ...
    val titleNodes = (xml \\ "item" \ "title") // todo: only reads titles right now
    val headlines = for {
      t <- titleNodes
    } yield t.text
    headlines.mkString(" ")
  }

}