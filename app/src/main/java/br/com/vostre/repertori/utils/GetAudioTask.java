package br.com.vostre.repertori.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.vostre.repertori.listener.GetAudioTaskListener;
import br.com.vostre.repertori.listener.TarefaAssincronaListener;

/**
 * Created by Almir on 24/02/2016.
 */
public class GetAudioTask extends AsyncTask<Object, Integer, File> {

    static InputStream is = null;
    String nomeArquivo = "";
    String umaURL = null;
    Context context = null;
    Map<String, Object> map = new HashMap<String, Object>();
    ProgressDialog progressDialog;
    GetAudioTaskListener listener;
    boolean isBackground;

    public GetAudioTask(String url, Context context, String nomeArquivo, boolean isBackground) {

        this.umaURL = url;
        this.nomeArquivo = nomeArquivo;
        this.context = context;
        this.isBackground = isBackground;

    }

    public void setListener(GetAudioTaskListener listener) {
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
    protected void onPostExecute(File resultado) {

        if (null != progressDialog && !isBackground) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        listener.onGetAudioTaskResultSucceeded(resultado);

    }

    @Override
    protected File doInBackground(Object... params) {
        // TODO Auto-generated method stub

        File pasta = new File(Constants.CAMINHO_PADRAO_AUDIO);

        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        try {
//            URL u = new URL(umaURL);
//            URLConnection conn = u.openConnection();
//            int contentLength = conn.getContentLength();
//
//            DataInputStream stream = new DataInputStream(u.openStream());
//
//            byte[] buffer = new byte[contentLength];
//            stream.readFully(buffer);
//            stream.close();
//
//            DataOutputStream fos = new DataOutputStream(new FileOutputStream(Constants.CAMINHO_PADRAO_AUDIO + File.separator + nomeArquivo));
//            fos.write(buffer);
//            fos.flush();
//            fos.close();

            URL u = new URL(umaURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(Constants.CAMINHO_PADRAO_AUDIO,nomeArquivo));


            InputStream in = c.getInputStream();

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ( (len1 = in.read(buffer)) > 0 ) {
                f.write(buffer,0, len1);
            }
            f.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new File(Constants.CAMINHO_PADRAO_AUDIO + File.separator + nomeArquivo);
    }
}
