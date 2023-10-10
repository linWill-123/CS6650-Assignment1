import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AlbumStore {
  private static final Map<String, Album> albums;

  public static void addAlbum(Album album) {
    albums.put(album.getId(), album);
  }

  public static Album getAlbum(String id) {
    return albums.get(id);
  }

  static {
    // Initialize the static albums map here with predefined data
    albums = new HashMap<>();
    String albumID = "1";
    byte[] image = null;
    Profile profile = new Profile("artistName", "titleName", "yearValue");
    Album album = new Album(albumID, profile, image);
    albums.put(albumID, album);
  }
}
