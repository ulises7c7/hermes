package com.modulus.ssc.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

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
import com.google.android.gms.maps.model.PolylineOptions;
import com.modulus.ssc.R;
import com.modulus.ssc.bl.DirectionsJSONParser;
import com.modulus.ssc.dao.DSLinea;
import com.modulus.ssc.model.Linea;
import com.modulus.ssc.model.Parada;
import com.modulus.ssc.model.Recorrido;
import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

//TODO: implementar patron estado
//TODO: una vez recomendada la linea, dibujar recorrido segun esta almacenado en svr
//TODO: una vez recomendadas las paradas, mostrarlas en el mapa y el recorrido desde el origen y hasta destino
//TODO: analizar si es neesaria la funcionalidad de elegir linea manualmente
public class ActivityMain1 extends Activity implements ConnectionCallbacks,

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
	// private PolygonOptions rectOptions;
	private MarkerOptions markerOptions = new MarkerOptions();
	private Marker markerOrig;
	private MarkerOptions markerOptionsDes = new MarkerOptions();
	private Marker markerDes;
	private MarkerOptions markerOptionsPO = new MarkerOptions();
	private Marker markerPO;
	private MarkerOptions markerOptionsPD = new MarkerOptions();
	private Marker markerPD;

	private static final int ESTADO_DEF_ORIGEN = 0;
	private static final int ESTADO_DEF_DESTINO = 1;
	private static final int ESTADO_REC_LINEA = 2;

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
		setContentView(R.layout.activity_main1);

		txtInstrucciones = (TextView) findViewById(R.id.txtInstrucciones);
		txtInstrucciones.setText(Html
				.fromHtml("<h1>Indica el origen de tu viaje</h1>"));

		buildGoogleApiClient();
		mGoogleApiClient.connect();

		lineas = obtenerLineas();
		// Linea linea = lineas.get(0);
		// rectOptions = obtenerPoligono(linea.getRecorrido());

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

		// Marcador Parada Origen
		markerOptionsPO.position(latLngDef);
		markerPO = myMap.addMarker(markerOptionsPO);
		markerPO.setTitle("Subida");

		// Marcador Parada Destino
		markerOptionsPD.position(latLngDef);
		markerPD = myMap.addMarker(markerOptionsPD);
		markerPD.setTitle("Descenso");

		// myMap.addPolygon(rectOptions);
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

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {

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

			// Recomendar Linea
			Linea lineaRecomendada = Linea.recomendarLinea(lineas,
					markerOrig.getPosition(), markerDes.getPosition());

			// Dibujar parada origen
			Parada pOrigen = lineaRecomendada.recomendarParada(markerOrig
					.getPosition());
			markerPO.setPosition(new LatLng(pOrigen.getLat(), pOrigen.getLng()));

			// Dibujar parada destino
			Parada pDestino = lineaRecomendada.recomendarParada(markerDes
					.getPosition());
			markerPO.setPosition(new LatLng(pDestino.getLat(), pDestino
					.getLng()));

			// Mover camara a parada origen
			// punto medio entre paradaOrigen y Origen, ajustando zoom
			animateCameraTo(pOrigen.getLat(), pOrigen.getLng());

			// Mostrar caminata hasta la parada de origen
			// Getting URL to the Google Directions API
			String url = getDirectionsUrl(markerOrig.getPosition(),
					markerPO.getPosition());
			DownloadTask downloadTask = new DownloadTask();
			// Start downloading json data from Google Directions API
			downloadTask.execute(url);

			/*
			 * 
			 * TODO: hacer que funcione para dar dos direcciones, seguramente
			 * esta intentando usar los mismos objetos para ambas rutas
			 * //Mostrar caminata desde la parada de destino // Getting URL to
			 * the Google Directions API String url2 =
			 * getDirectionsUrl(markerDes.getPosition(),
			 * markerPD.getPosition()); DownloadTask downloadTask2 = new
			 * DownloadTask(); // Start downloading json data from Google
			 * Directions API downloadTask.execute(url);
			 */

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

	/**
	 * Source:
	 * https://jigarlikes.wordpress.com/2013/04/26/driving-distance-and-travel
	 * -time-duration-between-two-locations-in-google-map-android-api-v2/
	 */

	private String getDirectionsUrl(LatLng origin, LatLng dest) {
		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {
		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {
			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();
			String distance = "";
			String duration = "";

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					if (j == 0) { // Get distance from the list
						distance = point.get("distance");
						continue;
					} else if (j == 1) { // Get duration from the list
						duration = point.get("duration");
						continue;
					}
					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);
					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.RED);
			}

			// tvDistanceDuration.setText("Distance:" + distance + ", Duration:"
			// + duration);

			// Drawing polyline in the Google Map for the i-th route
			myMap.addPolyline(lineOptions);
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// this.getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	private void dibujarRuta(LatLng origen, LatLng destino) {

	}

}
