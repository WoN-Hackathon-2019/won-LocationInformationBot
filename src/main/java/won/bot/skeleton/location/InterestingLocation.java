package won.bot.skeleton.location;

/**
 * An interesting location, with its title, wiki-page-id, and distance to the user.
 */
public class InterestingLocation {

    private String title;
    private String wikiPageId;
    private float distance;
    private String fullUrl;

    public InterestingLocation() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWikiPageId() {
        return wikiPageId;
    }

    public void setWikiPageId(String wikiPageId) {
        this.wikiPageId = wikiPageId;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    @Override
    public String toString() { //TODO: hier wurde geändert
        return  "title='" + title + '\'' +
                ", distance=" + distance +
                ", fullUrl=" + fullUrl;  //+ "\n"
    }
}
