package br.com.vostre.repertori.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.vostre.repertori.listener.TarefaAssincronaListener;

/**
 * Created by Almir on 24/02/2016.
 */
public class TarefaAssincrona extends AsyncTask<Object, Integer, Map<String, Object>> {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    Map<String, String> parametrosPost = new HashMap<>();
    String umaURL = null;
    String method = null;
    JSONArray jsonArray = null;
    Context context = null;
    Map<String, Object> map = new HashMap<String, Object>();
    String dataUltimoAcesso;
    ProgressDialog progressDialog;
    TarefaAssincronaListener listener;
    boolean isBackground;
    int acao;

    public TarefaAssincrona(String url, String method, Context context, Map<String, String> params, boolean isBackground, int acao) {

        this.umaURL = url;
        this.parametrosPost = params;
        this.method = method;
        this.context = context;
        this.isBackground = isBackground;
        this.acao = acao;

    }

    public void setOnResultListener(TarefaAssincronaListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {

        if (!isBackground) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Conectando ao Servidor...");
            progressDialog.setCancelable(false);
            //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
        }


    }

    @Override
    protected void onPostExecute(Map<String, Object> resultado) {

        if (null != progressDialog && !isBackground) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        listener.onTarefaAssincronaResultSucceeded(resultado, acao);

    }

    @Override
    protected Map<String, Object> doInBackground(Object... params) {
        // TODO Auto-generated method stub

        JSONObject jsonObj = null;
        StringBuilder resultado = new StringBuilder();

        // Making HTTP request
        try {
            // Making HTTP request
            // check for request method

            if (method.equals("POST")) {

                try {

                    HttpURLConnection conn;

                    if(acao == 1){
                        conn = HttpUtils.sendMultipartPostRequest(umaURL, parametrosPost);
                    } else{
                        conn = HttpUtils.sendPostRequest(umaURL, parametrosPost);
                    }

                    String[] resposta = HttpUtils.readMultipleLinesRespone();

                    HttpUtils.disconnect();

                    String json = resposta[0];
                    jsonObj = new JSONObject(json);

                    map.put("json", (Object) jsonObj);
                    map.put("metodo", "POST");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (method == "GET") {

                HttpURLConnection conn;

                if(acao == 2){
                    conn = HttpUtils.sendGetRequest(umaURL);
                } else{
                    conn = HttpUtils.sendGetRequest(umaURL);
                }



                String[] resposta = HttpUtils.readMultipleLinesRespone();

                HttpUtils.disconnect();

                String json = Arrays.toString(resposta);
                dataUltimoAcesso = conn.getHeaderField("Date");

                jsonArray = new JSONArray(json);
                jsonObj = jsonArray.getJSONObject(0);
                map.put("json", (Object) jsonObj);
                map.put("metodo", "GET");
                map.put("dataUltimoAcesso", dataUltimoAcesso);

            }

            // return JSONObject (this is a class variable and null is returned if something went bad)

        } catch (Exception e){
            Log.e("error-file", e.getLocalizedMessage());
        }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return map;

    }
}
