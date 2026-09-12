package com.codilar.empattendencetrack;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import java.text.DateFormat;
import java.util.Date;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Subscription;
import rx.functions.Action1;

import static android.text.format.DateFormat.getDateFormat;
import static android.text.format.DateFormat.getTimeFormat;

public class LocationService extends Service {

//    DateFormat dateFormat = getDateFormat(getApplicationContext());



    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    Subscription subscription;

    @Override
    public void onCreate() {
        super.onCreate();
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(getApplicationContext());
/*        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        txtLat.setText(String.valueOf(location.getLatitude()));
                        txtLong.setText(String.valueOf(location.getLongitude()));
                    }
                });*/

        LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1)
                .setInterval(1000);

        final Location clientLocation = new Location("location");
        clientLocation.setLatitude(12.9160772);
        clientLocation.setLongitude(77.5455674);
        // ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);
         subscription = locationProvider.getUpdatedLocation(request)
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        double dist = location.distanceTo(clientLocation);
//                        double dist = distance(clientLocation.getLatitude(), clientLocation.getLongitude(),
//                                location.getLatitude(), location.getLongitude());
                        if(dist<1){

                            DateFormat dateFormat = getDateFormat(getApplicationContext());
                            String dateInString = dateFormat.format(new Date());

                            DateFormat timeFormat= getTimeFormat(getApplicationContext());
                            String timeInString =timeFormat.format(new Date());


                            String message = "\n OUT  | " +dateInString+" | "+timeInString+ " "+ dist;
                            SaveToLog.saveLogToFile(getApplicationContext(),message);



                        }
                        else {

                            Toast.makeText(LocationService.this,"Service Started",Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),location.getLatitude()+ " "+location.getLongitude(), Toast.LENGTH_SHORT).show();

                            DateFormat dateFormat = getDateFormat(getApplicationContext());
                            String dateInString = dateFormat.format(new Date());

                            DateFormat timeFormat= getTimeFormat(getApplicationContext());
                            String timeInString =timeFormat.format(new Date());


                            Toast.makeText(LocationService.this, "You are inside the location", Toast.LENGTH_SHORT).show();
                            String message = "\n IN   | "+dateInString+" | "+timeInString+" | "+clientLocation.getLatitude()+" | "+clientLocation.getLongitude()+" " + dist;
                            SaveToLog.saveLogToFile(getApplicationContext(),message);


                        }
                       /* txtLat.setText(String.valueOf(location.getLatitude()));
                        txtLong.setText(String.valueOf(location.getLongitude()));*/


                    }

                });
    }

   /* void writeToNotePad(String message){
        try {
            File myFile = new File("/sdcard/mysdfile.txt");
           if (myFile.exists())

               myFile.createNewFile();

            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
            myOutWriter.write(message);
            myOutWriter.close();
            fOut.close();

            Toast.makeText(getBaseContext(),"Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }*/
  /* public class SaveToLog {

       public static final String FILE_NAME = "Location_log.txt";
      // public static final String DIR_NAME = "Yelligo_Logs";

    public  void saveLogToFile(Context context, String sBody) {
        try {
            boolean flag = false;

            File root = new File(String.valueOf(Environment.getExternalStorageDirectory()));
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, FILE_NAME);
            if (!gpxfile.exists()) {

                flag = true;
            }
            FileWriter writer = new FileWriter(gpxfile, true);

            writer.append(sBody);
            writer.flush();
            writer.close();
           
        } catch (IOException e) {
        }
    }
}*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Loc started", "");
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(LocationService.this, "Service Stopped", Toast.LENGTH_SHORT).show();
        subscription.unsubscribe();
    }

    public double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance;
    }

}
