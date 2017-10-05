package geography;

import java.awt.geom.Point2D.Double;

@SuppressWarnings("serial")
public class GeographicPoint extends Double {

    public GeographicPoint(final double latitude, final double longitude) {
        super(latitude, longitude);
    }

    /**
     * Calculates the geographic distance in km between this point and
     * the other point.
     *
     * @param other
     * @return The distance between this lat, lon point and the other point
     */
    public double distance(final GeographicPoint other) {
        return getDist(this.getX(), this.getY(),
                other.getX(), other.getY());
    }


    private double getDist(final double lat1, final double lon1, final double lat2, final double lon2) {
        int R = 6373; // radius of the earth in kilometres
        double lat1rad = Math.toRadians(lat1);
        double lat2rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1rad) * Math.cos(lat2rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public String toString() {
        return "Lat: " + getX() + ", Lon: " + getY();
    }


}
