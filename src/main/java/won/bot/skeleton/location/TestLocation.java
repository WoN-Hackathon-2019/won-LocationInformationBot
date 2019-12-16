package won.bot.skeleton.location;

public class TestLocation {
    public static void main(String[] args) {
        GDS testGDS = new GDS();
        System.out.println("List: " + testGDS.getCityByLngLat(16.363449, 48.210033)); //Vienna
        //System.out.println(testGDS.getCityByLngLat(11.576124,48.137154)); //Munich
        //System.out.println(testGDS.getCityByLngLat(12.4608,41.9015)); //Rome
        //System.out.println(testGDS.getCityByLngLat(13.055,47.8095)); //Salzburg
        //System.out.println("List: " + testGDS.getCityByLngLat(42, 42));
    }
}
