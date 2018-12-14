package kr.ac.hansung.thetherfinder;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    View rootView;
    MapView mapView;
    private DatabaseReference databaseReference;
    private FloatingActionButton floatingActionButton;
    public static final int REQUEST_CODE_PERMISSIONS = 1000;
    private final static String TAG = "MapFragment";
    private FloatingActionButton fab, fab2;
    private GoogleApiClient googleApiClient;
    private GoogleMap gmap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocoder geocoder;
    private Button button;
    private EditText editText;

    public MapFragment() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        editText = (EditText) rootView.findViewById(R.id.edFind);
        button = (Button) rootView.findViewById(R.id.btnFind);
        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.getMapAsync(this);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab2 = (FloatingActionButton) rootView.findViewById(R.id.fab2);
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

        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    void cgvshow() {
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

    void lotteshow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("어플이 존재하지 않습니다");
        builder.setMessage("플레이스토어로 이동할까요?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=kr.co.lottecinema.lcm")));
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

    void megashow() {
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
    void starshow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("어플이 존재하지 않습니다");
        builder.setMessage("플레이스토어로 이동할까요?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.starbucks.co")));
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.istarbucks.co.kr/index.do")));
                    }
                });
        builder.show();
    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(this.getActivity());
        geocoder = new Geocoder(getActivity());
        UiSettings ui = googleMap.getUiSettings();
        ui.setZoomControlsEnabled(true);
        ui.setCompassEnabled(true);
        //currentLocation();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.535779, 126.734406), 14);
        googleMap.animateCamera(cameraUpdate);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("star").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String Lnt = snapshot.child("Lng").getValue().toString();
                    String Lat = snapshot.child("Lat").getValue().toString();
                    // Object Lat = snapshot.eq("Lat");
                    String Location = snapshot.child("Location").getValue().toString();
                    System.out.println(Lnt);
                    Log.d(TAG, "result" + snapshot.getValue());
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.starbucks);
                    Bitmap b = bitmapDrawable.getBitmap();
                    Bitmap small = Bitmap.createScaledBitmap(b, 100, 100, false);
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lnt)))
                            .title(Location + " 스타벅스")
                            .icon(BitmapDescriptorFactory.fromBitmap(small)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("cgv").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String Lnt = snapshot.child("Lng").getValue().toString();
                    String Lat = snapshot.child("Lat").getValue().toString();
                    String Location = snapshot.child("Location").getValue().toString();
                    System.out.println(Lnt);
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lnt)))
                            .title(Location + " CGV")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("lotte").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String Lnt = snapshot.child("Lng").getValue().toString();
                    String Lat = snapshot.child("Lat").getValue().toString();
                    String Location = snapshot.child("Location").getValue().toString();
                    System.out.println(Lnt);
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lnt)))
                            .title(Location + " 롯데시네마")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("mega").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String megabox = " 메가박스";
                    String Lnt = snapshot.child("Lng").getValue().toString();
                    String Lat = snapshot.child("Lat").getValue().toString();
                    String Location = snapshot.child("Location").getValue().toString();
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lnt)))
                            .title(Location + " " + megabox)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("other").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String theather = snapshot.child("Theather").getValue().toString();
                    String Lnt = snapshot.child("Lng").getValue().toString();
                    String Lat = snapshot.child("Lat").getValue().toString();
                    String Location = snapshot.child("Location").getValue().toString();
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(Lat), Double.parseDouble(Lnt)))
                            .title(theather + " " + Location)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Context context = getActivity();
                Intent mega1 = context.getPackageManager().getLaunchIntentForPackage("com.megabox.mop");
                Intent lotte = context.getPackageManager().getLaunchIntentForPackage("kr.co.lottecinema.lcm");
                Intent cgv1 = context.getPackageManager().getLaunchIntentForPackage("com.cgv.android.movieapp");
                Intent star = context.getPackageManager().getLaunchIntentForPackage("com.starbucks.co");
                try {
                    String maker = marker.getTitle();
                    if (maker.contains("CGV")) {
                        if (cgv1 == null) {
                            cgvshow();

                        } else {
                            cgv1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(cgv1);
                        }

                    } else if (maker.contains("롯데시네마")) {
                        if (lotte == null) {
                            lotteshow();
                        } else {
                            lotte.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(lotte);
                        }

                    } else if (maker.contains("메가박스")) {
                        if (mega1 == null) {
                            megashow();
                        } else {
                            mega1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mega1);
                        }
                    }else if(maker.contains("스타벅스")) {
                        if(star == null){
                            starshow();
                        }else{
                            star.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(star);
                        }
                    }
                    else {
                        if (maker.contains("서울극장")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("서울극장을 선택하셨습니다");
                            builder.setMessage("웹페이지로 이동할까요?");
                            builder.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.seoulcinema.com/main/main.php")));
                                        }
                                    });
                            builder.setNegativeButton("아니오",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.show();
                        } else if (maker.contains("아트하우스모모")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("아트하우스 모모를 선택하셨습니다");
                            builder.setMessage("웹페이지로 이동할까요?");
                            builder.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.arthousemomo.co.kr/")));
                                        }
                                    });
                            builder.setNegativeButton("아니오",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.show();
                        } else if (maker.contains("씨네큐브")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("씨네큐브를 선택하셨습니다");
                            builder.setMessage("웹페이지로 이동할까요?");
                            builder.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cinecube.co.kr/")));
                                        }
                                    });
                            builder.setNegativeButton("아니오",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.show();
                        } else if (maker.contains("상상마당")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("KT&G 상상마당을 선택하셨습니다");
                            builder.setMessage("웹페이지로 이동할까요?");
                            builder.setPositiveButton("예",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sangsangmadang.com/")));
                                        }
                                    });
                            builder.setNegativeButton("아니오",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            builder.show();
                        } else {

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSIONS);


                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(),
                        new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(latLng)
                                            .title("나")
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                    googleMap.addCircle(new CircleOptions()
                                            .center(latLng)
                                            .radius(5000)
                                            .strokeColor(Color.BLUE)
                                            .strokeWidth(1.0f)
                                            .fillColor(Color.parseColor("#220000ff")));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
                                }
                            }
                        });
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {

                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {

                        Date now = new Date();
                        android.text.format.DateFormat.format("hh:mm:ss", now);
                        try {
                            // image naming and path  to include sd card  appending name you choose for file
                            // 저장할 주소 + 이름
                            String mPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Screenshots" + "/capture_" + now + ".jpg";

                            // create bitmap screen capture
                            // 화면 이미지 만들기
                            View v1 = getActivity().getWindow().getDecorView().getRootView();
                            v1.setDrawingCacheEnabled(true);

                            // 이미지 파일 생성
                            File imageFile = new File(mPath);
                            FileOutputStream outputStream = new FileOutputStream(imageFile);
                            int quality = 100;
                            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                            outputStream.flush();
                            outputStream.close();

                            Toast.makeText(getActivity(), mPath + "에 " + now + ".jpg 이름으로 저장되었습니다", Toast.LENGTH_LONG).show();
                        } catch (Throwable e) {
                            // Several error may come out with file handling or OOM
                            e.printStackTrace();
                        }

                    }
                };
                googleMap.snapshot(callback);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();
                ;
                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocationName(
                            str, 10);
                    System.out.println(addressList.get(0).toString());
                    //콤마를 기준으로 split
                    System.out.println(addressList.get(0).toString());

                    String[] splitStr = addressList.get(0).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length());
                    System.out.println(address);
                    String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                    System.out.println(latitude);
                    System.out.println(longitude);

                    // 좌표(위도, 경도) 생성
                    LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    googleMap.addMarker(new MarkerOptions()
                            .position(point)
                            .title(str)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    googleMap.addCircle(new CircleOptions()
                            .center(point)
                            .radius(500)
                            .strokeColor(Color.BLUE)
                            .strokeWidth(1.0f)
                            .fillColor(Color.parseColor("#220000ff")));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                } catch (NullPointerException e) {
                    Toast.makeText(getActivity(), "잘못된 입력입니다", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "/Pictures/*");
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }
}
