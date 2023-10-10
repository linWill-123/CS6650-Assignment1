import com.google.gson.JsonObject;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AlbumIdServlet", value = "/albums/*")
public class AlbumIdServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // Get url parameter information
    String pathInfo = request.getPathInfo();

    if (pathInfo != null) {
      // Retrieve the albumID
      String albumID = pathInfo.substring(1);
      /*For this assignment, just by default retrieve predefined data in AlbumStore, since we don't actually
       store the POST data into AlbumStore*/
      Album album = AlbumStore.getAlbum("1"); // Default id with 1 retrieved

      // If we can find an album with the given ID
      if (album != null) {
        response.setContentType("application/json");

        // Construct the JSON response
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("artist",album.getProfile().getArtist());
        jsonResponse.addProperty("title",album.getProfile().getTitle());
        jsonResponse.addProperty("year",album.getProfile().getYear());
        // Convert JsonObject to String and write to response
        response.getWriter().write(jsonResponse.toString());

      } else {
        // If we cannot find album with given ID
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("Album not found");
      }
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Invalid request");
    }
  }

}
