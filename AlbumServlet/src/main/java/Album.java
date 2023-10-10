public class Album {
  private String id;
  private Profile profile;
  private byte[] image;

  public Album(String id, Profile profile, byte[] image) {
    this.id = id;
    this.profile = profile;
    this.image = image;
  }

  public String getId() {
    return id;
  }

  public Profile getProfile() {
    return profile;
  }

  public byte[] getImage() {
    return image;
  }
}
