package com.awol.employeetracker.util;

import java.util.Locale;

public class GeofenceHelper {

    // Default Workplace / Office Geofence Coordinates
    public static final double DEFAULT_OFFICE_LAT = 12.916077;
    public static final double DEFAULT_OFFICE_LON = 77.545567;
    public static final float DEFAULT_RADIUS_METERS = 100.0f;

    /**
     * Calculates the distance between two geographical points using the Haversine formula.
     *
     * @return Distance in meters.
     */
    public static double calculateDistanceMeters(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371000; // in meters

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public static boolean isInsideGeofence(double currentLat, double currentLon,
                                          double officeLat, double officeLon,
                                          float radiusMeters) {
        double dist = calculateDistanceMeters(currentLat, currentLon, officeLat, officeLon);
        return dist <= radiusMeters;
    }

    public static String formatDistance(double meters) {
        if (meters < 1000) {
            return String.format(Locale.getDefault(), "%.0f meters", meters);
        } else {
            return String.format(Locale.getDefault(), "%.2f km", meters / 1000.0);
        }
    }
}
