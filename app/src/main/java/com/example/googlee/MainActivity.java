package com.example.googlee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{


    private GoogleMap mMap;
    private Marker currentMarker = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초


    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;


    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    Location mCurrentLocatiion;
    LatLng currentPosition;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;


    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mLayout = findViewById(R.id.layout_main);

        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d(TAG, "onMapReady :");

        mMap = googleMap;

        setDefaultLocation();



        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);



        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED   ) {


            startLocationUpdates(); // 3. 위치 업데이트 시작


        }else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                Snackbar.make(mLayout, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {


                        ActivityCompat.requestPermissions( MainActivity.this, REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();


            } else {

                ActivityCompat.requestPermissions( this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }



        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d( TAG, "onMapClick :");
            }
        });


            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions
                    .position(new LatLng(37.330062, 126.836433))
                    .title("푸드뱅크1377")
                    .snippet("+82314014819");

            mMap.addMarker(makerOptions);

        MarkerOptions makerOptions001 = new MarkerOptions();
        makerOptions001
                .position(new LatLng(37.888445, 127.093680))
                .title("동두천천사푸드뱅크")
                .snippet("+82318678291");
        mMap.addMarker(makerOptions001);

        MarkerOptions makerOptions002 = new MarkerOptions();
        makerOptions002
                .position(new LatLng(37.824694, 126.984335))
                .title("동두천천사푸드뱅크")
                .snippet("+82318371377");
        mMap.addMarker(makerOptions002);

        MarkerOptions makerOptions003 = new MarkerOptions();
        makerOptions003
                .position(new LatLng(37.776865, 126.726681))
                .title("희망나눔터(푸드뱅크)")
                .snippet("+82319453054");
        mMap.addMarker(makerOptions003);

        MarkerOptions makerOptions004 = new MarkerOptions();
        makerOptions004
                .position(new LatLng(37.740606, 126.487438))
                .title("강화푸드뱅크")
                .snippet("+82329321378");
        mMap.addMarker(makerOptions004);

        MarkerOptions makerOptions005 = new MarkerOptions();
        makerOptions005
                .position(new LatLng(37.649585, 127.235132))
                .title("푸드뱅크")
                .snippet("+82315956169");
        mMap.addMarker(makerOptions005);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.536061, 127.204497))
                .title("하남푸드뱅크")
                .snippet("+82317906920"); //경기도 하남시 덕풍동 461-10
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.432497, 127.126433))
                .title("(사단)성남푸드뱅크")
                .snippet("+82317561378"); //경기도 성남시 중원구 성남동 4367
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.447590, 126.883316))
                .title("광명푸드뱅크")
                .snippet("+8228051377"); //경기도 광명시 소하동 1341-1
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.474506, 126.644734))
                .title("인천 동구 푸드마켓")
                .snippet("+82327732377"); //인천광역시 동구 송림동
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.474396, 126.627238))
                .title("중구푸드뱅크")
                .snippet("+82327611399"); //인천광역시 중구 내동 3-5
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.357051, 126.937807))
                .title("군포푸드뱅크")
                .snippet("+82313972054"); //경기도 군포시 금정동 870-10
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.291889, 127.506024))
                .title("이천푸드뱅크")
                .snippet("+82316341377"); //경기도 이천시 부발읍 신원리 561-20
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.151211, 127.071133))
                .title("오산시푸드뱅크")
                .snippet("+82313781377"); //경기도 오산시 오산동 853-32
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.890219, 126.633178))
                .title("당진군푸드뱅크")
                .snippet("+82413574343"); //충청남도 당진시 읍내동 172-4
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.708579, 126.840429))
                .title("예산군기초푸드뱅크")
                .snippet("+82413341377"); //충청남도 예산군 예산읍 관작리 222
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.773493, 127.064693))
                .title("아산시기초푸드뱅크")
                .snippet("+82415341377"); //충청남도 아산시 배방읍 북수리 707-74
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.854121, 127.438093))
                .title("진천군푸드뱅크")
                .snippet("+82435331377"); //충청북도 진천군 진천읍 읍내리 435-7
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.639295, 127.439234))
                .title("푸드뱅크")
                .snippet("+82432341377"); //충청북도 청주시 흥덕구 복대1동 262
        mMap.addMarker(makerOptions);

        //강원도
        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(38.450235, 128.452442))
                .title("고성푸드뱅크사랑나눔센타")
                .snippet("+82336811377"); //강원도 고성군 거진읍 거진리 226-2
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(38.450235, 128.452442))
                .title("고성푸드뱅크사랑나눔센타")
                .snippet("+82336811377"); //강원도 고성군 거진읍 거진리 226-2
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(38.102419, 127.986424))
                .title("양구사랑나눔복지회푸드뱅크")
                .snippet("+82334820999"); //강원도 양구군 양구읍 정림리 189-1
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(38.111698, 127.705189))
                .title("푸드뱅크")
                .snippet("+82334421377"); //강원도 화천군 화천읍 상리 20-47
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.830586, 127.512973))
                .title("가평푸드뱅크")
                .snippet("+827088472436"); //경기도 가평군 가평읍 읍내리 470-1
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.824704, 126.984341))
                .title("양주기초푸드뱅크")
                .snippet("+82318371377"); //경기도 양주시 광적면 가납리 737-1
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.759346, 128.901802))
                .title("강릉기초푸드뱅크")
                .snippet("+82336431377"); //강원도 강릉시 옥천동 265
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.982775, 128.370633))
                .title("단양군푸드뱅크")
                .snippet("+82434221299"); //충청북도 단양군 단양읍 도전리 632
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.922213, 128.351614))
                .title("단양나눔과기쁨푸드뱅크")
                .snippet("+8270775601937"); //충청북도 단양군 대강면 장림리 149-2
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(37.138153, 128.206317))
                .title("제천시푸드뱅크")
                .snippet("+82436471417"); //충청북도 제천시 서부동 206
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.870617, 127.442572))
                .title("진천나눔과기쁨푸드뱅크")
                .snippet("+82435360129"); //충청북도 진천군 진천읍 성석리 480-13
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.854121, 127.438082))
                .title("진천군푸드뱅크")
                .snippet("+82435331377"); //충청북도 진천군 진천읍 읍내리 435-7
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.722397, 127.534434))
                .title("청원군기초푸드뱅크")
                .snippet("+82432181377"); //충청북도 청원군 내수읍 마산리
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.637800, 127.467044))
                .title("청주시사랑나눔기초푸드뱅크")
                .snippet("+82432661377"); //충청북도 청주시 흥덕구 사창동 245-34
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.639295, 127.439230))
                .title("푸드뱅크")
                .snippet("+82432341377"); //충청북도 청주시 흥덕구 복대1동 262
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.456193, 127.128680))
                .title("성공회푸드뱅크")
                .snippet("+82418530329"); //충청남도 공주시 산성동 69-35
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.773490, 127.064683))
                .title("아산시기초푸드뱅크")
                .snippet("+82415341377"); //충청남도 아산시 배방읍 북수리 707-74
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.708579, 126.840426))
                .title("예산군기초푸드뱅크")
                .snippet("+82413341377"); //충청남도 예산군 예산읍 관작리 222
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.890214, 126.633186))
                .title("당진군푸드뱅크")
                .snippet("+82413574343"); //충청남도 당진시 읍내동 172-4
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.772363, 126.461758))
                .title("서산시기초푸드뱅크")
                .snippet("+82416672308"); //충청남도 서산시 수석동 서해로 3456
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.483518, 126.829053))
                .title("청양푸드뱅크")
                .snippet("+82419441500"); //충청남도 청양군 운곡면 위라리 484-1
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.324515, 127.422882))
                .title("대전광역시푸드뱅크")
                .snippet("+82425251378"); //대전광역시 중구 대흥동 452-3
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.313016, 127.437146))
                .title("푸드뱅크")
                .snippet("+82422422700"); //대전광역시 중구 문창동 381-26
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.187821, 127.090267))
                .title("논산사랑 푸드뱅크")
                .snippet("+82417361004"); //충청남도 논산시 강산동 134-1
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.120946, 128.127275))
                .title("김천푸드뱅크")
                .snippet("+82544302191"); //경상북도 김천시 모암동 140-1
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.120808, 128.143443))
                .title("글로벌희망나누리푸드뱅크")
                .snippet("+82544326009"); //경상북도 김천시 지좌동 440-114
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.151042, 128.315366))
                .title("사랑나눔푸드뱅크")
                .snippet("+82544411377"); //경상북도 구미시 봉곡동 294-2
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.730432, 128.451996))
                .title("달성군푸드뱅크")
                .snippet("+82536161376"); //대구광역시 달성군 논공읍 북리 1-104
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.841468, 128.602744))
                .title("푸드뱅크")
                .snippet("+82534721400"); //대구광역시 남구 봉덕2동 1019
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.874336, 128.638949))
                .title("진명푸드뱅크")
                .snippet("+82537461377"); //효목동 466-1번지 동구 대구광역시 KR
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.110707, 129.346267))
                .title("흥해제일기초푸드뱅크")
                .snippet("+82542617525"); //경상북도 포항시 북구 흥해읍 옥성리 24
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.065185, 129.378326))
                .title("내일을여는집푸드뱅크")
                .snippet("+82542441377"); //경상북도 포항시 북구 장성동 1216
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.046036, 129.318962))
                .title("우리푸드뱅크")
                .snippet("+82542551377"); //경상북도 포항시 북구 흥해읍 대련리 385-6
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.033580, 129.376545))
                .title("포항푸드뱅크")
                .snippet("+82542310847"); //경상북도 포항시 남구 송도동 436-57
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.539011, 129.317305))
                .title("울산광역시나눔푸드마켓")
                .snippet("+82522521377"); //울산광역시 남구 달동 637-10
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.566447, 129.125078))
                .title("울주푸드마켓")
                .snippet("+82522241377"); //울산광역시 울주군 언양읍 동부리
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.230667, 128.694852))
                .title("창원시기초푸드뱅크")
                .snippet("+82552531377"); //경상남도 창원시 의창구 신월동 13-67
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.248263, 128.600982))
                .title("푸드뱅크")
                .snippet("+82552989108"); //경상남도 창원시 마산회원구 구암2동 31
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(34.943111, 126.974265))
                .title("화순군푸드뱅크")
                .snippet("+82613711377"); //전라남도 화순군 춘양면 우봉리 495-1
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.167429, 126.913793))
                .title("행복나눔푸드뱅크")
                .snippet("+82622691377"); //광주광역시 북구 중흥동 802-39
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.177282, 126.878975))
                .title("신일작은도서관섬기고나눔푸드뱅크")
                .snippet("+82625241377"); //광주광역시 북구 운암동
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.125623, 126.929607))
                .title("동구기초푸드뱅크")
                .snippet("+82622341377"); //광주광역시 동구 용산동 3
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.160343, 126.805146))
                .title("푸드뱅크광산구지부")
                .snippet("+82629451377"); //광주광역시 광산구 우산동 1606-12
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.869105, 127.130795))
                .title("전주시중앙푸드뱅크")
                .snippet("+82632542758"); //전라북도 전주시 덕진구 사근1길 23-25 KR
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.797126, 127.135046))
                .title("전주푸드마켓")
                .snippet("+82632861377"); //전라북도 전주시 완산구 평화동1가 592-25
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(35.786186, 127.131543))
                .title("푸드뱅크")
                .snippet("+82632254215"); //전라북도 전주시 완산구 평화동2가 345
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.065185, 129.378326))
                .title("내일을여는집푸드뱅크")
                .snippet("+82542441377"); //경상북도 포항시 북구 장성동 1216
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.065185, 129.378326))
                .title("내일을여는집푸드뱅크")
                .snippet("+82542441377"); //경상북도 포항시 북구 장성동 1216
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.065185, 129.378326))
                .title("내일을여는집푸드뱅크")
                .snippet("+82542441377"); //경상북도 포항시 북구 장성동 1216
        mMap.addMarker(makerOptions);

        makerOptions = new MarkerOptions();
        makerOptions
                .position(new LatLng(36.065185, 129.378326))
                .title("내일을여는집푸드뱅크")
                .snippet("+82542441377"); //경상북도 포항시 북구 장성동 1216
        mMap.addMarker(makerOptions);

     //   }



    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);
                //location = locationList.get(0);

                currentPosition
                        = new LatLng(location.getLatitude(), location.getLongitude());


                String markerTitle = getCurrentAddress(currentPosition);
                String markerSnippet = "위도:" + location.getLatitude()
                        + " 경도:" + location.getLongitude();

                Log.d(TAG, "onLocationResult : " + markerSnippet);


                //현재 위치에 마커 생성하고 이동
                setCurrentLocation(location, markerTitle, markerSnippet);

                mCurrentLocatiion = location;
            }


        }

    };



    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION);



            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED   ) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");

        if (checkPermission()) {

            Log.d(TAG, "onStart : call mFusedLocationClient.requestLocationUpdates");
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap!=null)
                mMap.setMyLocationEnabled(true);

        }


    }


    @Override
    protected void onStop() {

        super.onStop();

        if (mFusedLocationClient != null) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }




    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0);
        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {


        if (currentMarker != null) currentMarker.remove();


        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);

    }


    public void setDefaultLocation() {


        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";


        if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mMap.moveCamera(cameraUpdate);

    }


    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        return hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED;

    }




    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {


            boolean check_result = true;



            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                startLocationUpdates();
            }
            else {


                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                }else {


                    Snackbar.make(mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }

        }
    }


    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_ENABLE_REQUEST_CODE) {//사용자가 GPS 활성 시켰는지 검사
            if (checkLocationServicesStatus()) {
                if (checkLocationServicesStatus()) {

                    Log.d(TAG, "onActivityResult : GPS 활성화 되있음");


                    needRequest = true;

                    return;
                }
            }
        }
    }



}