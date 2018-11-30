package kr.ac.hansung.thetherfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.PackageManager;
import android.widget.Toast;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.Double.parseDouble;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap Cgvm, Megam, Lottem;
    View rootView;
    MapView mapView;
    GoogleMap mGooglemap;
    private DatabaseReference databaseReference;
    ArrayList<Theather> theatherList = new ArrayList<Theather>();
    private final static String TAG = "MapFragment";
    public static String cgv = "https://theater-finder-a5dbc.firebaseio.com/cgv.json?auth=ruxlDZcSd0SLipBnIq73xxNv70c1pR4z1iADtnwy";
    public static String megabox = "https://theater-finder-a5dbc.firebaseio.com/mega.json?auth=ruxlDZcSd0SLipBnIq73xxNv70c1pR4z1iADtnwy";
    public static String lotte = "https://theater-finder-a5dbc.firebaseio.com/lotte.json?auth=ruxlDZcSd0SLipBnIq73xxNv70c1pR4z1iADtnwy";
    public MapFragment(){

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView)rootView.findViewById(R.id.mapView);
        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mapView !=null){
            mapView.onCreate(savedInstanceState);
        }
    }
    void cgvshow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("어플이 존재하지 않습니다");
        builder.setMessage("플레이스토어로 이동할까요?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.cgv.android.movieapp")));
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cgv.co.kr/")));
                    }
                });
        builder.show();
    }
    void lotteshow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("어플이 존재하지 않습니다");
        builder.setMessage("플레이스토어로 이동할까요?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=kr.co.lottecinema.lc")));
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.lottecinema.co.kr/LCHS/index.aspx")));
                    }
                });
        builder.show();
    }
    void megashow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("어플이 존재하지 않습니다");
        builder.setMessage("플레이스토어로 이동할까요?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.megabox.mop")));
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.megabox.co.kr/")));
                    }
                });
        builder.show();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(this.getActivity());

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.535779,126.734406), 14);
        googleMap.animateCamera(cameraUpdate);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("cgv").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String Lnt = snapshot.child("Lng").getValue().toString();
                    String Lat = snapshot.child("Lat").getValue().toString();
                   // Object Lat = snapshot.eq("Lat");
                    String Location = snapshot.child("Location").getValue().toString();
                    System.out.println(Lnt);
                    Log.d(TAG, "result"+snapshot.getValue());
                   googleMap.addMarker(new MarkerOptions()
                   .position(new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lnt)))
                    .title(Location + "CGV")
                           .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                   googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                       @Override
                       public void onInfoWindowClick(Marker marker) {
                           Context context = getActivity();
                           Intent cgv1 = context.getPackageManager().getLaunchIntentForPackage("com.cgv.android.movieapp");

                           try{
                               if(lotte == null){
                                   cgvshow();
                               }else{
                                   cgv1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                   startActivity(cgv1);
                               }
                           }catch(Exception e){
                               e.printStackTrace();
                           }
                       }
                   });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("lotte").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String Lnt = snapshot.child("Lng").getValue().toString();
                    String Lat = snapshot.child("Lat").getValue().toString();
                    // Object Lat = snapshot.eq("Lat");
                    String Location = snapshot.child("Location").getValue().toString();
                    System.out.println(Lnt);
                    Log.d(TAG, "result"+snapshot.getValue());
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lnt)))
                            .title(Location + "롯데시네마")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Context context = getActivity();
                            Intent lotte = context.getPackageManager().getLaunchIntentForPackage("kr.co.lottecinema.lc");

                            try{
                                if(lotte == null){
                                    lotteshow();
                                }else{
                                    lotte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(lotte);
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("mega").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String Lnt = snapshot.child("Lng").getValue().toString();
                    String Lat = snapshot.child("Lat").getValue().toString();
                    // Object Lat = snapshot.eq("Lat");
                    String Location = snapshot.child("Location").getValue().toString();
                    System.out.println(Lnt);
                    Log.d(TAG, "result"+snapshot.getValue());
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lnt)))
                            .title(Location + "메가박스")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Context context  = getActivity();
                            Intent mega1 = context.getPackageManager().getLaunchIntentForPackage("com.megabox.mop");

                            try{
                                    if(mega1 == null){
                                        megashow();
                                    }else{
                                        mega1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(mega1);
                                    }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /*public void CaptureMapScreen() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;
                try {
                    FileOutputStream out = new FileOutputStream("/storage/emulated/0/1.png");

                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        gMap.snapshot(callback);

        Intent intent = new Intent(this, SubActivity.class);
        startActivity(intent);
    }*/
}
