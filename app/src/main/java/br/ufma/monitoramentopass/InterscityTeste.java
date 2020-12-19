package br.ufma.monitoramentopass;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class InterscityTeste {
    public static final String TAG = InterscityTeste.class.getSimpleName();

    public static InterscityTeste instance;
    public RequestQueue requestQueue;
    public static Context ctx;
    final String[] text = new String[100];

    public <T> void addToRequestQueue(Request<T> request){ getRequestQueue().add(request);}

    public static synchronized InterscityTeste getInstance(Context context) {
        if (instance == null) {
            instance = new InterscityTeste(context);
        }
        return instance;
    }

    public InterscityTeste(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        Log.i(TAG,"###### Conectando Interscity");

        // Access the RequestQueue through your singleton class.
        //Interscity.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public void getRequest(){
        String url = "http://cidadesinteligentes.lsdi.ufma.br/adaptor/subscriptions/3d0a4d48-2f34-4561-8975-29c8521bb828";

        // uuid: 3d0a4d48-2f34-4561-8975-29c8521bb828

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        text[0] = response.toString();
                        Log.i(TAG,"####### Interscity: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.i(TAG,"####### ERRO - Interscity: " + error.getMessage());
                    }
                });

        addToRequestQueue(jsonObjectRequest);
    }

    public void post(LocalizacaoPass dadosPassageiro){
        String url = "http://cidadesinteligentes.lsdi.ufma.br/adaptor/resources/7d604d0a-c54e-470d-8cc3-c2245d524110/data/localizacao";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("{}")){
                            Log.i(TAG,"##### Resultado:[envio dados com sucesso!] " + response);
                        }
                        else{
                            Log.i(TAG,"##### Resultado:[erro no envio dos dados] " + response);
                        }
//                        JSONArray jsonArray = null;
//                        try {
//                            jsonArray = new JSONArray(response);
//                        }
//                        catch (JSONException e){
//                            e.printStackTrace();
//                        }
//
//                        JSONObject r = new JSONObject();
//                        JSONArray j;
//                        try{
//                            for(int i=0; i<jsonArray.length(); i++){
//                                JSONObject passageiro = jsonArray.getJSONObject(i);
//                                String latitude = passageiro.getString("latitude");
//                                String longitude = passageiro.getString("longitude");
//                                String altitude = passageiro.getString("altitude");
//                                String velocidade = passageiro.getString("velocidade");
//
//                                Log.i(TAG,"###### Resposta do Servidor: " + latitude + longitude + altitude + velocidade);
//                            }
//                        }
//                        catch (JSONException e){
//                            e.printStackTrace();
//                        }
//
//                        Log.i(TAG,"##### Resposta inteira do servidor: " + response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {

                try {
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
                    df.setTimeZone(tz);
                    String nowAsISO = df.format(new Date());

                    JSONObject params = new JSONObject();
                    params.put("identificador",dadosPassageiro.getIdentificador());
                    params.put("latitude",dadosPassageiro.getLatitude());
                    params.put("longitude", dadosPassageiro.getLongitude());
                    params.put("altitude", dadosPassageiro.getAltitude());
                    params.put("velocidade", dadosPassageiro.getVelocidade());
                    params.put("latitudeDestino", dadosPassageiro.getLatitudeDestino());
                    params.put("longitudeDestino", dadosPassageiro.getLongitudeDestino());
                    params.put("timestamp", nowAsISO);

                    JSONArray array = new JSONArray();
                    array.put(params);

                    JSONObject json = new JSONObject();

                    json.put("data", array);

                    Log.i(TAG,"##### DADOS DO SMARTPHONE: " + json);

                    return json.toString().getBytes(StandardCharsets.UTF_8);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return super.getBody();
            }

//            @Override
//            public Map<String, String> getParams(){
//                TimeZone tz = TimeZone.getTimeZone("UTC");
//                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
//                df.setTimeZone(tz);
//                String nowAsISO = df.format(new Date());
//
//                Map<String, String>  data = new HashMap<>();
//
//                Map<String, String>  params = new HashMap<>();
//                params.put("identificador",dadosPassageiro.getIdentificador());
//                params.put("localizacao",dadosPassageiro.getLatitude());
//                params.put("longitude", dadosPassageiro.getLongitude());
//                params.put("altitude", dadosPassageiro.getAltitude());
//                params.put("velocidade", dadosPassageiro.getVelocidade());
//                params.put("timestamp", nowAsISO);
//
//                //Log.i(TAG,"##### DATA: " + nowAsISO);
//
//                Log.i(TAG,"##### JSON ANTIGO: " + params.toString());
//
//
//
//                data.put("data", (new String[] {params.toString()}).toString() );
//
//                Log.i(TAG,"##### JSON NOVO: " + data.toString());
//
//                return params;
//            }
        };
        //addToRequestQueue(postRequest);
        requestQueue.add(postRequest);
    }
}
