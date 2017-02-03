package br.com.vostre.repertori;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.vostre.repertori.listener.TarefaAssincronaListener;
import br.com.vostre.repertori.service.AtualizaDadosService;
import br.com.vostre.repertori.utils.Constants;
import br.com.vostre.repertori.utils.DialogUtils;
import br.com.vostre.repertori.utils.TarefaAssincrona;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, TarefaAssincronaListener {

    GoogleApiClient mGoogleApiClient;
    SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.

//        //Check if connected
//        if(App.getGoogleApiHelper().isConnected())
//        {
            //Get google api client
            mGoogleApiClient = App.getGoogleApiHelper().getGoogleApiClient();
//            client.
//        }

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("login", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();

            Map<String, String> map = new HashMap<>();
            map.put("idToken", acct.getIdToken());

            TarefaAssincrona tarefaToken = new TarefaAssincrona(Constants.URLTOKENGOOGLE, "POST", LoginActivity.this, map, true);

            tarefaToken.setOnResultListener(this);
            tarefaToken.execute();

            //logou
        } else {
            // Signed out, show unauthenticated UI.
            Log.d("login", "Erro: "+result.getStatus().getStatusMessage());
        }
    }

    @Override
    public void onTarefaAssincronaResultSucceeded(Map<String, Object> map) {

        if(map == null || map.size() == 0){
            android.app.AlertDialog alertDialog = DialogUtils.criarAlerta(this, "Erro ao Fazer Login",
                    "Não foi possivel fazer login no sistema. Pode ter havido instabilidade na rede ou o usuário não foi encontrado. " +
                            "Por favor tente novamente.");

            alertDialog.show();
            return;
        }

        JSONObject jObj = (JSONObject) map.get("json");

        int status = 0;

        if(jObj != null){
            try {
                JSONArray metadados = jObj.getJSONArray("meta");
                JSONObject objMetadados = metadados.getJSONObject(0);

                status = objMetadados.getInt("status");

                if(status == 200){
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("usuario", acct);
                    startActivity(intent);
                } else{
                    Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Houve algum problema ao fazer login... Por favor tente novamente.", Toast.LENGTH_LONG).show();
        }

    }
}
