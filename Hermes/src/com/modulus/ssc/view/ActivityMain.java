package com.modulus.ssc.view;

import java.text.DateFormat;
import java.util.ArrayList;
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
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.modulus.ssc.R;
import com.modulus.ssc.dao.DSLinea;
import com.modulus.ssc.model.Linea;
import com.modulus.ssc.model.Recorrido;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ActivityMain extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener, LocationListener, OnMapClickListener,
		OnClickListener {
	public static final String TAG_SSC = "Modulus";
	private GoogleApiClient mGoogleApiClient;
	private Location mLastLocation;
	private LocationRequest mLocationRequest;
	private GoogleMap myMap;

	// Utilizado para saber si los servicios de ubicacion estan encendidos
	private boolean mRequestingLocationUpdates = true;
	// Momento de la ultima actualizacion de la posicion
	private String mLastUpdateTime;
	private PolygonOptions rectOptions;
	private MarkerOptions markerOptions = new MarkerOptions();
	private MarkerOptions markerOptionsDes = new MarkerOptions();

	private static final int ESTADO_DEF_ORIGEN = 0;
	private static final int ESTADO_DEF_DESTINO = 1;
	private Marker markerDes;
	private Marker markerOrig;

	private float defZoom = 15.2f;

	private TextView txtInstrucciones;

	private LatLng latLngresistencia = new LatLng(-27.451151, -58.986433);
	private LatLng latLngDef = new LatLng(32.536389, 44.420833);

	private List<Linea> lineas;

	private int estado;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inicializar();
		inicializarMapa();
	}

	private void inicializar() {
		setContentView(R.layout.activity_main);

		txtInstrucciones = (TextView) findViewById(R.id.txtInstrucciones);
		txtInstrucciones.setText(Html
				.fromHtml("<h1>Indica el origen de tu viaje</h1>"));

		buildGoogleApiClient();
		mGoogleApiClient.connect();

		lineas = obtenerLineas();
		Linea linea = lineas.get(0);
		rectOptions = obtenerPoligono(linea.getRecorrido());

	}

	private void inicializarMapa() {
		myMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		myMap.setOnMapClickListener(this);

		LatLng initLatLng;
		if (mLastLocation == null) {
			initLatLng = latLngresistencia;
		} else {
			initLatLng = new LatLng(mLastLocation.getLatitude(),
					mLastLocation.getLongitude());
		}

		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initLatLng, defZoom));

		// Marcador Origen
		markerOptions.position(latLngDef);
		markerOrig = myMap.addMarker(markerOptions);
		markerOrig.setTitle("Origen");

		// Marcador Destino
		markerOptionsDes.position(latLngDef);
		markerDes = myMap.addMarker(markerOptionsDes);
		markerDes.setTitle("Destino");

		myMap.addPolygon(rectOptions);
	}

	protected synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
		createLocationRequest();
	}

	// Se inicializa el Location request
	// Objeto que se encarga de conseguir la ultima ubicacion conocida
	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(10000);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

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
		// if (mLastLocation != null) {
		// LatLng latLng = new LatLng(mLastLocation.getLatitude(),
		// mLastLocation.getLongitude());
		// markerCurr.setPosition(latLng);
		//
		// }

	}

	private PolygonOptions obtenerPoligono(Recorrido recorrido) {
		ArrayList<LatLng> puntos = new ArrayList<LatLng>();
		for (int i = 0; i < recorrido.getPuntos().size(); i++) {
			double latitude = recorrido.getPuntos().get(i).getLat();
			double longitude = recorrido.getPuntos().get(i).getLng();
			LatLng p = new LatLng(latitude, longitude);
			puntos.add(p);
		}
		PolygonOptions poligono = new PolygonOptions().addAll(puntos);

		return poligono;

	}

	@Override
	public void onMapClick(LatLng latlng) {

		switch (estado) {
		case ESTADO_DEF_ORIGEN:
			markerOrig.setPosition(latlng);
			estado = ESTADO_DEF_DESTINO;
			txtInstrucciones.setText(Html
					.fromHtml("<h1>Indica el destino de tu viaje</h1>"));
			break;
		case ESTADO_DEF_DESTINO:
			markerDes.setPosition(latlng);
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {

	}

	public void animateCameraTo(final double lat, final double lng) {

		CameraPosition camPosition = myMap.getCameraPosition();
		if (!((Math.floor(camPosition.target.latitude * 100) / 100) == (Math
				.floor(lat * 100) / 100) && (Math
				.floor(camPosition.target.longitude * 100) / 100) == (Math
				.floor(lng * 100) / 100))) {
			myMap.getUiSettings().setScrollGesturesEnabled(false);
			myMap.animateCamera(
					CameraUpdateFactory.newLatLng(new LatLng(lat, lng)),
					new CancelableCallback() {

						@Override
						public void onFinish() {
							myMap.getUiSettings()
									.setScrollGesturesEnabled(true);

						}

						@Override
						public void onCancel() {
							myMap.getUiSettings().setAllGesturesEnabled(true);

						}
					});
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putDouble("oLat", markerOrig.getPosition().latitude);
		outState.putDouble("oLng", markerOrig.getPosition().longitude);
		outState.putDouble("dLat", markerDes.getPosition().latitude);
		outState.putDouble("dLng", markerDes.getPosition().longitude);

		outState.putDouble("camLat", myMap.getCameraPosition().target.latitude);
		outState.putDouble("camLng", myMap.getCameraPosition().target.longitude);
		outState.putFloat("camZoom", myMap.getCameraPosition().zoom);

		outState.putCharSequence("txtInst", txtInstrucciones.getText());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Double oLat = savedInstanceState.getDouble("oLat");
		Double oLng = savedInstanceState.getDouble("oLng");
		Double dLat = savedInstanceState.getDouble("dLat");
		Double dLng = savedInstanceState.getDouble("dLng");
		Double camLat = savedInstanceState.getDouble("camLat");
		Double camLng = savedInstanceState.getDouble("camLng");
		Float camZoom = savedInstanceState.getFloat("camZoom");
		CharSequence txtInst = savedInstanceState.getCharSequence("txtInst");

		txtInstrucciones.setText(txtInst);
		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(camLat,
				camLng), camZoom));
		markerDes.setPosition(new LatLng(dLat, dLng));
		markerOrig.setPosition(new LatLng(oLat, oLng));
	}

	private List<Linea> obtenerLineas() {
		DSLinea dsLinea;
		dsLinea = new DSLinea(this);
		dsLinea.open();
		List<Linea> ls = dsLinea.getAll();
		dsLinea.close();
		return ls;
	}

	@Override
	public void onBackPressed() {
		switch (estado) {
		case ESTADO_DEF_ORIGEN:
			super.onBackPressed();
			break;
		case ESTADO_DEF_DESTINO:
			estado--;
			animateCameraTo(markerOrig.getPosition().latitude,
					markerOrig.getPosition().longitude);
			txtInstrucciones.setText(Html
					.fromHtml("<h1>Indica el origen de tu viaje</h1>"));
			break;

		default:
			break;
		}
	}

}
