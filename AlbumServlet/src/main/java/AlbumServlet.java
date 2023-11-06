import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

@WebServlet(name = "AlbumServlet", value = "/albums")
@MultipartConfig(
        fileSizeThreshold = 1024*1024*10,
        maxFileSize = 1024*1024*50,
        maxRequestSize = 1024*1024*100
)
public class AlbumServlet extends HttpServlet {

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    // Early exit if the content type is not as expected
    if (!request.getContentType().startsWith("multipart/form-data")) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid content type");
      return;
    }

    // We will parse parts only if needed, avoiding unnecessary processing
    Part imagePart = request.getPart("image"); // This throws if not present
    Part profilePart = request.getPart("profile"); // This throws if not present

    // Now we are sure that we have the parts we can start processing
    byte[] image = IOUtils.toByteArray(imagePart.getInputStream());
    String profileContent = new String(IOUtils.toByteArray(profilePart.getInputStream()), StandardCharsets.UTF_8);

    Profile profile;
    try {
      JsonObject profileJson = JsonParser.parseString(profileContent).getAsJsonObject();
      profile = new Profile(
              profileJson.get("artist").getAsString(),
              profileJson.get("title").getAsString(),
              profileJson.get("year").getAsString());
    } catch (JsonSyntaxException e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid profile JSON format");
      return;
    }

    // Here you should have some logic to store the album, currently it's just being created
    String albumID = UUID.randomUUID().toString();
    // Assume Album is a valid class that can store this information
    Album album = new Album(albumID, profile, image);

    // Prepare and send response
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();

    JsonObject jsonResponse = new JsonObject();
    jsonResponse.addProperty("albumID", albumID);
    jsonResponse.addProperty("imageSize", image.length);

    writer.write(jsonResponse.toString());
    writer.flush();
    writer.close();
  }
}
