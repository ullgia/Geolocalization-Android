package com.example.giacomo.venicewifi;

import android.Manifest;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.giacomo.venicewifi.R.id.credits;
import static com.example.giacomo.venicewifi.R.id.design_navigation_view;
import static com.example.giacomo.venicewifi.R.id.driveButton;
import static com.example.giacomo.venicewifi.R.id.map;
import static com.example.giacomo.venicewifi.R.id.transitButton;
import static com.example.giacomo.venicewifi.R.id.walkButton;

public class ActivityMaps extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener,NavigationView.OnNavigationItemSelectedListener  {
    private String TAG = ActivityInfo.class.getSimpleName();
    public static boolean venice = true;
    public static boolean reggio = true;
    public static boolean rome= true;
    public static boolean milan = true;
    public static boolean genova = true;
    public static boolean perugia = true;
    public static boolean lecce = true;
    String lingua ="";
    String titolo ="";
    MenuItem defCity;
    MenuItem anotherCity ;
    MenuItem defLanguage ;
    MenuItem openTutorials;
    RadioButton pubblico;
    RadioButton piedi;
    RadioButton macchina;
    private ListView lv;
    ArrayList<HashMap<String, String>> directionList;
    public String cityToDisplay="";
    public static String jString = "";
    public String pos = "";
    public String travelMode = "driving";
    public SimpleLocation location;
    static Polyline polyline = null;
    public static GoogleMap mMap;
    public static ProgressDialog pDialog;
    static final Integer LOCATION = 0x1;
    public boolean gps = true;
    // URL to get contacts JSON
    public static ArrayList<HashMap<String, String>> hotspotList = new ArrayList<>();
    public static ClusterManager<MyMarker> mClusterManager;
    public LocationManager managerGps;
    public CheckIfInternetIsAvaible internet;
    private BottomSheetBehavior mBottomSheetBehavior;
    private int diplayHeight;
    Button buttonMaps;
    Button buttonDirections;
    public static String currentCity="";
    public static String languageDefault ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent i = getIntent();
        cityToDisplay = i.getExtras().getString("city");
        languageDefault = loadDataDefautl("lingua");
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        diplayHeight = size.y;
        directionList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.isHideable();
        mBottomSheetBehavior.setPeekHeight(0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nv = navigationView.getMenu();
        defCity = nv.findItem(R.id.def_city);
        anotherCity = nv.findItem(R.id.another_city);
        defLanguage = nv.findItem(R.id.language);
        openTutorials = nv.findItem(R.id.tutorials);
        macchina = (RadioButton)findViewById(R.id.driveButton);
        piedi = (RadioButton) findViewById(R.id.walkButton);
        pubblico = (RadioButton)findViewById(R.id.transitButton);
        navigationView.setNavigationItemSelectedListener(this);
        buttonMaps = (Button) findViewById(R.id.goWithMaps);
        buttonMaps.setOnClickListener(this);
        // define mMap as a new googleMap
        buttonDirections = (Button) findViewById(R.id.getDirections);
        buttonDirections.setOnClickListener(this);

        setMenuItemTitle();
        startTask();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public String loadDataDefautl(String stringa){
        SharedPreferences sharedPref= getSharedPreferences("mypref", 0);
        return  sharedPref.getString(stringa, "");
    }
    public void setMenuItemTitle(){
        lingua = loadDataDefautl("lingua");
        switch (lingua){
            case "":
                defCity.setTitle("Choose the Default City");
                anotherCity.setTitle("Display Another City");
                defLanguage.setTitle("Select the Default Language");
                openTutorials.setTitle("Tutorials");
                macchina.setText("Drive");
                piedi.setText("Walk");
                pubblico.setText("Public Transit");
                buttonDirections.setText("Get Directions");
                buttonMaps.setText("Go With Maps");
                break;
            case "inglese":
                defCity.setTitle("Choose the Default City");
                anotherCity.setTitle("Display Another City");
                defLanguage.setTitle("Select the Default Language");
                openTutorials.setTitle("Tutorials");
                macchina.setText("Drive");
                piedi.setText("Walk");
                pubblico.setText("Public Transit");
                buttonMaps.setText("Go With Maps");
                buttonDirections.setText("Get Directions");
                break;
            case "italiano":
                defCity.setTitle("Selezionare la città predefinita");
                anotherCity.setTitle("Visualizzare altre città");
                defLanguage.setTitle("Scegliere la Lingua predefinita");
                openTutorials.setTitle("Tutorials");
                macchina.setText("Guida");
                piedi.setText("A Piedi");
                pubblico.setText("Mezzi Pubblici");
                buttonDirections.setText("Ottieni Indicazioni");
                buttonMaps.setText("Vai con Google Maps");
                break;
            case "spagnolo":
                defCity.setTitle("Elige la ciudad predeterminada");
                anotherCity.setTitle("Mostrar otra ciudad");
                defLanguage.setTitle("Seleccione el idioma predeterminado");
                openTutorials.setTitle("Abrir tutorial");
                macchina.setText("Conducir");
                piedi.setText("Andar");
                pubblico.setText("Transporte público");
                buttonMaps.setText("Ir con Google Maps");
                buttonDirections.setText("Obtener Indicaciones");
                break;
            case "portoghese":
                defCity.setTitle("Escolha a cidade padrão");
                anotherCity.setTitle("Mostrar outra cidade");
                defLanguage.setTitle("Selecione o idioma padrão");
                openTutorials.setTitle("Tutorials");
                macchina.setText("Conduzir");
                piedi.setText("Andar");
                pubblico.setText("Transporte públicos");
                buttonMaps.setText("Abrir com Google Maps");
                buttonDirections.setText("Obter Indicações");

                break;


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(ActivityMaps.this, ActivityDefaultCity.class);
            ActivityMaps.this.startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.def_city) {

            Intent i = new Intent(ActivityMaps.this, ActivityDefaultCity.class);
            ActivityMaps.this.startActivity(i);
          } else if (id == R.id.another_city) {
            Intent i = new Intent(ActivityMaps.this, ActivitySelectCity.class);
            ActivityMaps.this.startActivity(i);

        } else if (id == R.id.credits) {
            Intent i = new Intent(ActivityMaps.this, ActivityCredits.class);
            ActivityMaps.this.startActivity(i);

        }else if (id == R.id.tutorials){
            Intent i = new Intent(ActivityMaps.this, ActivityTutorialCity.class);
            ActivityMaps.this.startActivity(i);        }
        else if(id == R.id.language){
            Intent i = new Intent(ActivityMaps.this, ActivitySelectLanguage.class);
            ActivityMaps.this.startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startTask(){
        switch (cityToDisplay){
            case "venice":
                GetHotspotVenice taskVenice = new GetHotspotVenice(ActivityMaps.this);
                taskVenice.execute();
                break;
            case "rome":
                GetHotspotRome taskRome = new GetHotspotRome(ActivityMaps.this);
                taskRome.execute();
                break;
            case "milan":
                GetHotspotMilan taskMilan = new GetHotspotMilan(ActivityMaps.this);
                taskMilan.execute();
                break;
            case "bologna":
                GetHotspotBologna taskBologna = new GetHotspotBologna(ActivityMaps.this);
                taskBologna.execute();
                break;
            case "reggio":
                GetHotspotReggioEmilia taskReggio = new GetHotspotReggioEmilia(ActivityMaps.this);
                taskReggio.execute();
                break;
            case "genova":
                GetHotspotGenova taskGenova = new GetHotspotGenova(ActivityMaps.this);
                taskGenova.execute();
                break;
            case "perugia":
                GetHotspotPerugia taskPerugia = new GetHotspotPerugia(ActivityMaps.this);
                taskPerugia.execute();
                break;
            case "lecce":
                GetHotspotLecce  tasklecce = new GetHotspotLecce(ActivityMaps.this);
                tasklecce.execute();
                break;


        }
    }
    @Override
    public void onClick(View v) {
        internet = new CheckIfInternetIsAvaible();
        if (gps && location.getLatitude() != 0.0f && location.getLongitude() != 0.0f && pos != "") {
            switch (v.getId()) {
                case R.id.goWithMaps:

                    if (internet.isNetworkAvaible(ActivityMaps.this)) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("https://maps.google.com/maps?saddr=" + location.getLatitude() + "," + location.getLongitude() + "&daddr=" + pos));
                        startActivity(intent);
                    } else
                        Toast.makeText(ActivityMaps.this, "To get Directions Internet is needed", Toast.LENGTH_LONG);
                    break;
                case R.id.getDirections:
                    if (internet.isNetworkAvaible(ActivityMaps.this)) {
                        Intent i = new Intent(ActivityMaps.this, ActivityInfo.class);
                        i.putExtra("directions", jString);
                        startActivity(i);
                    } else
                        Toast.makeText(ActivityMaps.this, "To get Directions Internet is needed", Toast.LENGTH_LONG);

            }
        }
    }
    public void askForGPS(){
        PromptForGps askGPS = new PromptForGps(ActivityMaps.this);
        askGPS.askForGpsActivation();
    }
    public void setUpAll(){
        // a map is created
        mClusterManager = new ClusterManager<MyMarker>(this, getMap());
        managerGps = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //declared a radio group for the travel mode
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.modeGroup);
        radioGroup.check(driveButton);
        //a new button to start the maps navigation







        GetOriginPoint firstPoint = new GetOriginPoint(ActivityMaps.this, gps, location);
        firstPoint.getOriginPointMarker(cityToDisplay);
        // Add cluster items (markers) to the cluster manager.
        // disable default maps navigation button
        mMap.getUiSettings().setMapToolbarEnabled(false);
        final CheckIfInternetIsAvaible internet = new CheckIfInternetIsAvaible();
        //ask for GPS and Internet permissions
        AskForPermissions ask = new AskForPermissions();
        ask.askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION, ActivityMaps.this);
        ask.askForPermission(Manifest.permission.INTERNET, 1, ActivityMaps.this);
        if (!managerGps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gps = false;
            askForGPS();

            // Call your Alert message
        }
        location = new SimpleLocation(ActivityMaps.this);

        //if gps permission is active display point on map

        //check if internet connection is present to display the data
        if (internet.isNetworkAvaible(ActivityMaps.this))
            Toast.makeText(ActivityMaps.this, "Loading Online Data", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(ActivityMaps.this, "Loading Offline Data", Toast.LENGTH_LONG).show();
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        mMap.setOnInfoWindowClickListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        try {
            mClusterManager.setRenderer(new MyClusterRenderer(this, mMap, mClusterManager));
        } catch (Exception e) {
        }
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyMarker>() {

            public boolean onClusterClick(Cluster<MyMarker> cluster) {
                // Show a toast with some info when the cluster is clicked.
                // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
                // inside of bounds, then animate to center of the bounds.
                // Create the builder to collect all essential cluster items for the bounds.
                if (ContextCompat.checkSelfPermission(ActivityMaps.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    ActivityMaps.mMap.setMyLocationEnabled(true);
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (ClusterItem item : cluster.getItems()) {
                    builder.include(item.getPosition());
                }
                // Get the LatLngBounds
                final LatLngBounds bounds = builder.build();
                // Animate camera to the bounds
                try {
                    getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyMarker>() {
            @Override
            public boolean onClusterItemClick(final MyMarker myMarker) {
                internet.isNetworkAvaible(ActivityMaps.this);
                if (ContextCompat.checkSelfPermission(ActivityMaps.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    ActivityMaps.mMap.setMyLocationEnabled(true);
                int zoomLevel = 12;
                if(mMap.getCameraPosition().zoom < zoomLevel)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myMarker.getPosition(),zoomLevel));
                if (polyline != null) {
                    CleanPolyline poly = new CleanPolyline(ActivityMaps.this, polyline);
                    poly.cleanPoyline();
                }
                if (myMarker.stato.contains("ko")) {
                    Toast.makeText(ActivityMaps.this, myMarker.name + " Hotspot not working", Toast.LENGTH_SHORT).show();// display toast
                    mBottomSheetBehavior.setPeekHeight(0);

                } else {
                    mBottomSheetBehavior.setPeekHeight((int)(diplayHeight/4));
                    View bottomSheet = findViewById(R.id.bottom_sheet);

                    GetDirectionsList direction = new GetDirectionsList(myMarker);
                    direction.execute();
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);

                    behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            //            mBottomSheetBehavior.setPeekHeight(0);
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                            mBottomSheetBehavior.setPeekHeight(400);
                        }
                    });

                    if (!managerGps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        gps = false;
                    } else {
                        gps = true;
                    }
                    if (gps)
                        try {
                            location.beginUpdates();
                        } catch (Exception e) {
                        }
                    // if the marker is working we show hotspot info
                    //a request of location update is send to gps
                    pos = myMarker.getPosition().latitude + "," + myMarker.getPosition().longitude;
                    //if location is different from null diplay a route
                    final MyMarker hotSpotMarker = myMarker;
                    if (gps && location.getLatitude() != 0.0f && location.getLongitude() != 0.0f &&
                            internet.isNetworkAvaible(ActivityMaps.this)) {
                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    CleanPolyline polyli = new CleanPolyline(ActivityMaps.this, polyline);
                                    try {
                                        polyline.remove();
                                    }catch (Exception e){}

                                // checkedId is the RadioButton selected
                                if (checkedId == transitButton) {
                                    travelMode = "transit";
                                    polyli.cleanPoyline();
                                    Toast.makeText(ActivityMaps.this, " Transit", Toast.LENGTH_SHORT).show();// display toast
                                } else if (checkedId == walkButton) {
                                    travelMode = "walking";
                                    polyli.cleanPoyline();
                                    Toast.makeText(ActivityMaps.this, " Walking", Toast.LENGTH_SHORT).show();// display toast
                                } else {
                                    travelMode = "driving";
                                    polyli.cleanPoyline();
                                    Toast.makeText(ActivityMaps.this, " Driving", Toast.LENGTH_SHORT).show();// display toast
                                }
                                try {
                                    polyline.remove();
                                }catch (Exception e){}
                                GetDirectionsList directions = new GetDirectionsList(myMarker);
                                directions.execute();
                                if (ContextCompat.checkSelfPermission(ActivityMaps.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                                    ActivityMaps.mMap.setMyLocationEnabled(true);
                            }
                        });

                    }
                }
                return true;

            }

        });

    }
    public class GetDirectionsList extends AsyncTask<Void, Void, Void> {
        MyMarker myMarker;
    public GetDirectionsList(MyMarker marker){
        super();
        myMarker=marker;

    }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ActivityMaps.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            GetRoute newRoute1 = new GetRoute(ActivityMaps.this, myMarker.getPosition(), travelMode);
            newRoute1.getRoute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //  HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            //  String jsonStr = sh.makeServiceCall(url);
            if (jString != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jString);

                    // Getting JSON Array node
                    JSONArray routes = jsonObj.getJSONArray("routes");

                    // looping through All features
                    for (int i = 0; i < routes.length(); i++) {
                        JSONArray legs = ((JSONObject)routes.get(i)).getJSONArray("legs");
                        for(int j = 0; j < legs.length(); ++j) {
                            JSONArray steps = ((JSONObject) legs.get(j)).getJSONArray("steps");
                            for(int k = 0; k < steps.length(); ++k) {
                                String fragmentLenght= (String)((JSONObject)((JSONObject)steps.get(k)).get("distance")).get("text");
                                String fragmentTime  = (String)((JSONObject)((JSONObject)steps.get(k)).get("duration")).get("text");
                                String fragmentHTML  = fromHtml((String)(((JSONObject)steps.get(k)).get("html_instructions"))).toString();
                                HashMap<String, String> directions = new HashMap<>();

                                // adding each child node to HashMap key => value
                                directions.put("html",fragmentHTML );
                                directions.put("time",fragmentTime);
                                directions.put("lenght",fragmentLenght);
                                // adding contact to contact list
                                directionList.add(directions);
                            }
                        }
                    }
                } catch (final JSONException e) {}
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    ActivityMaps.this, directionList,
                    R.layout.list_item, new String[]{"html","lenght","time"}, new int[]{R.id.html
                    , R.id.lenght, R.id.time});

            lv.setAdapter(adapter);
        }

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
    public GoogleMap getMap() {
        return mMap;
    }

    public class MyClusterRenderer extends DefaultClusterRenderer<MyMarker> {

        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());

        public MyClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MyMarker> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyMarker item, MarkerOptions markerOptions) {
            BitmapDescriptor markerDescriptor;
            if (!item.stato.contains("ko")) {
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
            } else
                markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            markerOptions.icon(markerDescriptor);
        }

       @Override
        protected void onClusterItemRendered(MyMarker clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
        }
 }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpAll();
    }


}