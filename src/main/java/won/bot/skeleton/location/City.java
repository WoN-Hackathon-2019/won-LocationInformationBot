package won.bot.skeleton.location;

import java.util.ArrayList;
import java.util.List;

public class City {
    private double longitude;
    private double latitude;
    private String name;
    private String country;
    private String region;
    private List<InterestingLocation> interestingLocations;
    private String englishName;
    private String capital;
    private Integer population;
    private Integer area;
    private List<String> callingCodes;
    private List<String> topLevelDomain;
    private List<String> timezones;

    public City() {
        interestingLocations = new ArrayList<>();
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public List<String> getCallingCodes() {
        return callingCodes;
    }

    public void setCallingCodes(List<String> callingCodes) {
        this.callingCodes = callingCodes;
    }

    public List<String> getTopLevelDomain() {
        return topLevelDomain;
    }

    public void setTopLevelDomain(List<String> topLevelDomain) {
        this.topLevelDomain = topLevelDomain;
    }

    public List<String> getTimezones() {
        return timezones;
    }

    public void setTimezones(List<String> timezones) {
        this.timezones = timezones;
    }

    public List<InterestingLocation> getInterestingLocations() {
        return interestingLocations;
    }

    public void setInterestingLocations(List<InterestingLocation> interestingLocations) {
        this.interestingLocations = interestingLocations;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    private String intLocsPrintPretty() { //TODO: hier wurde geändert
        String pretty = "\n";

        int count = 0;
        if (interestingLocations.size() == 0) {
            return "no interesting locations found";
        } else if (interestingLocations.size() < 5) {
            count = interestingLocations.size();
        } else {
            count = 5; //amount of locations shown
        }

        for (int i = 0; i < count; i++) {
            pretty += this.interestingLocations.get(i).toString() + "\n";
        }

        return pretty;
    }

    private String populationPrintPretty() { //TODO: hier wurde geändert
        StringBuilder builder = new StringBuilder(Long.toString(this.population));
        int count = 1;
        List<Integer> intList = new ArrayList<>();

        for (int i = builder.length(); i>0;i--) {
            if (count % 3 == 0) {
                intList.add(builder.length() - (count));
            }
            count++;
        }

        for (Integer place:intList) {
            builder.insert(place,".");
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return  "## " + name + " (longitude:" + longitude + ", latitude:" + latitude + ")\n" +
                "---\n" +
                "*Name(English)*: " + englishName + "\n" +
                "*Country*: " + country + "\n" +
                "*Region*: " + region + "\n" +
                "*Capital*: " + capital + "\n" +
                "*Population*: " + populationPrintPretty() + " People\n" +
                "*Area*: " + area + "m² \n" +
                "*CallingCodes*: " + "+" + callingCodes + "\n" +
                "*TopLevelDomain*: " + topLevelDomain + "\n" +
                "*Timezones*: " + timezones + "\n  \n" +
                "## Interesting Locations: \n" + intLocsPrintPretty();
    }
}
