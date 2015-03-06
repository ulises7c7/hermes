package com.modulus.ssc.view;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.modulus.ssc.R;
import com.modulus.ssc.dao.DSEmpresa;
import com.modulus.ssc.model.Empresa;
import com.modulus.ssc.test.DemoDataLoader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ActivityMain extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, LocationListener {
	public static final String TAG_SSC = "Modulus";
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	private LocationRequest mLocationRequest;
	private GoogleMap myMap;
	private DSEmpresa dsEmpresa;
	// Utilizado para saber si los servicios de ubicacion estan encendidos
	private boolean mRequestingLocationUpdates = true;
	// Momento de la ultima actualizacion de la posicion
	private String mLastUpdateTime;

	private PolygonOptions rectOptions = new PolygonOptions().add(
			new LatLng(-27.4559121, -58.98296710000001), 
			new LatLng(-27.448809700000005,-58.97528530000001), 
			new LatLng(-27.4496094, -58.9743948),
			new LatLng(-27.445563, -58.9699531), 
			new LatLng(-27.442382799999997, -58.97347210000001), 
			new LatLng(-27.4439634, -58.97519950000001), 
			new LatLng(-27.442363800000003, -58.97696969999999), 
			new LatLng(-27.449619, -58.9847803), 
			new LatLng(-27.4511899,-58.9830101), 
			new LatLng(-27.4535606, -58.9856386),
			new LatLng(-27.4559121, -58.98296710000001));

	private CircleOptions currentLocationCircleOptions = new CircleOptions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG_SSC, "Se llama al metodo inicializar()");
		inicializar();
		Log.i(TAG_SSC, "Se establece el content view");
		setContentView(R.layout.activity_main);

//		Log.i(TAG_SSC, "Se instancia al data loader");
//		DemoDataLoader dataLoader = new DemoDataLoader(this);
//		Log.i(TAG_SSC, "Se invoca al data loader");
//		dataLoader.load();

		Log.i(TAG_SSC, "Se carga el view del map");
		myMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		Log.i(TAG_SSC, "Se crea el polygon del recorrido");
		myMap.addPolygon(rectOptions);

		
		Log.i(TAG_SSC, "current location circle options: "
				+ currentLocationCircleOptions.getCenter());

		Log.i(TAG_SSC, "Se establece posicion de la camara");
		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				-27.451151, -58.986433), 14.0f));
		Log.i(TAG_SSC, "Se establece tipo de mapa");
		myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		LatLng testLatLng = new LatLng(-27.4559121, -58.98296710000001);
		currentLocationCircleOptions = new CircleOptions().center(testLatLng)
				.radius(100).strokeColor(Color.RED).fillColor(Color.BLUE);
		// myMap.addCircle();

		myMap.addCircle(currentLocationCircleOptions);
		
		Log.e(TAG_SSC, "On create - valor de mGoogleApiClient:" + mGoogleApiClient);
		mGoogleApiClient.connect();

	}

	private void inicializar() {
		buildGoogleApiClient();
		
		dsEmpresa = new DSEmpresa(this);
		dsEmpresa.open();
		

		//Empresa empresa = new Empresa();
		//empresa.setNombre("Ataco");
		//long id = dsEmpresa.create(empresa);
		//empresa.setId(id);
		//Log.e(TAG_SSC, "Se ha insertado la empresa con el id " + id);

		//Context context = getApplicationContext();
		//CharSequence text = "Se ha insertado la empresa " + empresa.getNombre()	+ " con el id " + id;
		//int duration = Toast.LENGTH_LONG;

		//Toast toast = Toast.makeText(context, text, duration);
		//toast.show();

		List<Empresa> empresas = dsEmpresa.getAll();
		Log.e(TAG_SSC, "Se han encontrado " + empresas
				+ " empresas en la base de datos-");
		
		if (empresas != null && empresas.size() >= 1){
			Log.i(TAG_SSC, "La primera empresa encontrada es: " + empresas.get(0).getNombre());
		}

	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
		createLocationRequest();
		
		Log.e(TAG_SSC, "se ha llamado a buildGoogleApiClient: " + mGoogleApiClient);
		
	}

	// Se inicializa el Location request
	// Objeto que se encarga de conseguir la ultima ubicacion conocida
	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	//
	protected void startLocationUpdates() {
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, mLocationRequest, this);
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLastLocation = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		if (mRequestingLocationUpdates) {
			startLocationUpdates();
		}

		Log.i(TAG_SSC, "On connected - Valor de mLastLocation: "
				+ mLastLocation);

		updateUI();
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		mLastLocation = location;
		mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
		updateUI();

	}

	private void updateUI() {
		if (mLastLocation != null) {
			LatLng latLng = new LatLng(mLastLocation.getLatitude(),
					mLastLocation.getLongitude());
			currentLocationCircleOptions.center(latLng);
			myMap.addCircle(currentLocationCircleOptions);
		}
		
		Log.e(TAG_SSC, "se ha llamado a udpadeUI: " + currentLocationCircleOptions.getCenter().latitude + "," + currentLocationCircleOptions.getCenter().longitude );
	}

}
