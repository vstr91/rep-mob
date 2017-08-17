package br.com.vostre.repertori.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import br.com.vostre.repertori.service.AtualizaDadosService;

/**
 * Created by Almir on 11/03/2015.
 */
public class ServiceUtils {

    public boolean isMyServiceRunning(Class<?> serviceClass, ActivityManager manager) {
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void iniciaServicoAtualizacao(boolean consideraData, Context context){

        if(consideraData){
            String ultimoAcesso = ParametrosUtils.getDataUltimoAcesso(context);
            Calendar dataUltimoAcesso;

            if(!ultimoAcesso.equals("-")){
                dataUltimoAcesso = DataUtils.bancoParaDataComEspaco(ultimoAcesso);
            } else{
                dataUltimoAcesso = Calendar.getInstance();
                dataUltimoAcesso.add(Calendar.YEAR, -1);
            }

            Calendar agora = Calendar.getInstance();

            agora.add(Calendar.HOUR_OF_DAY, -1);
            System.out.println("Acesso: "+DataUtils.toString(dataUltimoAcesso, true));
            System.out.println("Agora: "+DataUtils.toString(agora, true));

            if(dataUltimoAcesso.before(agora) ){
                Intent serviceIntent = new Intent(context, AtualizaDadosService.class);
                context.stopService(serviceIntent);
                context.startService(serviceIntent);
            }
        } else{
            Intent serviceIntent = new Intent(context, AtualizaDadosService.class);
             context.stopService(serviceIntent);
            context.startService(serviceIntent);
        }



//        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        Intent serviceIntent = new Intent(this, AtualizaDadosService.class);
//        PendingIntent pi = PendingIntent.getService(this, 0, serviceIntent, 0);
//
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), TimeUnit.MINUTES.toMillis(Constants.TEMPO_ATUALIZACAO), pi);

        //

//        final ServiceUtils serviceUtils = new ServiceUtils();
//        Intent serviceIntent = new Intent(getBaseContext(), AtualizaDadosService.class);
//
//        final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//
//        if(!serviceUtils.isMyServiceRunning(AtualizaDadosService.class, manager)){
//            stopService(serviceIntent);
//            startService(serviceIntent);
//            //Toast.makeText(this, "Iniciando serviço...", Toast.LENGTH_LONG).show();
//        } else{
//            //Toast.makeText(this, "Serviço já rodando...", Toast.LENGTH_LONG).show();
//        }

    }

}
