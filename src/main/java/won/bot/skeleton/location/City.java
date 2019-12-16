package won.bot.skeleton.location;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class City {
    private double longitude;
    private double latitude;
    private String name;
    private String country;
    private String region;
    private List<InterestingLocation> interestingLocations;

    public City(){
        interestingLocations = new ArrayList<>();
    }

    private String englishName;
    private String capital;
    private Integer population;
    private Integer area;
    private List<Integer> callingCodes;
    private String topLevelDomain;
    private List<String> timezones;


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

    public List<Integer> getCallingCodes() {
        return callingCodes;
    }

    public void setCallingCodes(List<Integer> callingCodes) {
        this.callingCodes = callingCodes;
    }

    public String getTopLevelDomain() {
        return topLevelDomain;
    }

    public void setTopLevelDomain(String topLevelDomain) {
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

    private String intLocsPrintPretty(){ //TODO: hier wurde geändert
        String pretty = "\n";

        int count = 0;
        if (interestingLocations.size() == 0){
            return "no interesting locations found";
        }else if (interestingLocations.size() < 5){
            count = interestingLocations.size();
        }else {
            count = 5; //amount of locations shown
        }

        for (int i = 0; i < count; i++){
            pretty += this.interestingLocations.get(i).toString() + "\n";
        }
        /*
        for (InterestingLocation intLoc: this.interestingLocations) {
        pretty += "\n" +       intLoc.getTitle() + " " +
                               intLoc.getDistance() + "m" +
                  "\n" +       intLoc.getFullUrl() + "\n";
        }*/

        return pretty;
    }

    @Override
    public String toString() {
        return "City{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", englishName='" + englishName + '\'' +
                ", capital='" + capital + '\'' +
                ", population=" + population +
                ", area=" + area +
                ", callingCodes=" + callingCodes +
                ", topLevelDomain='" + topLevelDomain + '\'' +
                ", timezones=" + timezones +
                ", interestingLocations=" + intLocsPrintPretty() + //interestingLocations.toString() +
                '}';
    }
}
