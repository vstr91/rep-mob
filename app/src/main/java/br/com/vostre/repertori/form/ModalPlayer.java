package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import br.com.vostre.repertori.EventoDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.TempoMusicaEvento;
import br.com.vostre.repertori.utils.Constants;
import br.com.vostre.repertori.utils.DataUtils;
import br.com.vostre.repertori.utils.GetAudioTask;

public class ModalPlayer extends android.support.v4.app.DialogFragment implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    Button btnFechar;
    ImageButton btnPlay;
    SeekBar seekBarDuracao;
    TextView textViewMusica;
    TextView textViewArtista;
    TextView textViewDuracao;
    TextView textViewTempoAtual;
    TempoMusicaEvento tme;

    boolean isPlaying = false;
    boolean isPaused = false;
    MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    View view;
    Runnable runnable;

    public TempoMusicaEvento getTme() {
        return tme;
    }

    public void setTme(TempoMusicaEvento tme) {
        this.tme = tme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.modal_player, container, false);

        view.setMinimumWidth(700);

        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        textViewMusica = (TextView) view.findViewById(R.id.textViewMusica);
        textViewArtista = (TextView) view.findViewById(R.id.textViewArtista);
        textViewDuracao = (TextView) view.findViewById(R.id.textViewDuracao);
        textViewTempoAtual = (TextView) view.findViewById(R.id.textViewTempoAtual);
        seekBarDuracao = (SeekBar) view.findViewById(R.id.seekBarDuracao);

        textViewMusica.setText(tme.getMusicaEvento().getMusica().getNome());
        textViewArtista.setText(tme.getMusicaEvento().getMusica().getArtista().getNome());
        textViewDuracao.setText(DataUtils.toStringSomenteHoras(tme.getTempo(), 1));

        btnFechar.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        seekBarDuracao.setOnSeekBarChangeListener(this);

        return view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btnFechar:
                dismiss();
                break;
            case R.id.btnPlay:

                if(isPlaying){

                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        btnPlay.setImageResource(R.drawable.ic_play);
                        isPlaying = false;
                        isPaused = true;
                    }

                } else {

                    if (isPaused) {
                        mediaPlayer.start();
                        btnPlay.setImageResource(R.drawable.ic_pause);
                        isPaused = false;
                    } else {
                        File f = new File(Constants.CAMINHO_PADRAO_AUDIO + File.separator + tme.getAudio());

                        if (f.exists()) {
                            mediaPlayer = MediaPlayer.create(getContext(), Uri.fromFile(f));

                            if(mediaPlayer == null){
                                Toast.makeText(getContext(), "Houve algum erro ao processar áudio. Por favor tente novamente.", Toast.LENGTH_SHORT).show();
                                return;
                            }

//                            mediaController = new MediaController(getContext());
                            mediaPlayer.setOnPreparedListener(this);
                            mediaPlayer.setOnCompletionListener(this);

                            try {
                                //mediaPlayer.setDataSource(f.getAbsolutePath());
                                seekBarDuracao.setMax(mediaPlayer.getDuration());
                                //mediaPlayer.prepare();

                                if(seekBarDuracao.getProgress() > 0){
                                    mediaPlayer.seekTo(seekBarDuracao.getProgress());
                                }

                                mediaPlayer.start();
//                                mediaController.setMediaPlayer(this);
//                                mediaController.show();
                                isPlaying = true;
                                btnPlay.setImageResource(R.drawable.ic_pause);

                                runnable = new Runnable() {

                                    @Override
                                    public void run() {
                                        try{
                                            if (mediaPlayer != null && isPlaying) {
                                                int mCurrentPosition = mediaPlayer.getCurrentPosition();// / 1000;
                                                seekBarDuracao.setProgress(mCurrentPosition);
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.setTime(new Date(mCurrentPosition));
                                                textViewTempoAtual.setText(DataUtils.toStringSomenteHoras(calendar, 1));
                                                //System.out.println("TEMPO >>>>>>>>>>>> " + mCurrentPosition);
                                            }
                                            handler.postDelayed(runnable, 1000);
                                        } catch(IllegalStateException ex){
                                            try {
                                                this.finalize();
                                            } catch (Throwable throwable) {
                                                throwable.printStackTrace();
                                            }
                                        }
                                    }
                                };

                                //Make sure you update Seekbar on UI thread
                                this.getActivity().runOnUiThread(runnable);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(getContext(), "Áudio não encontrado. Pode ter sido renomeado ou movido.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                break;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if(mediaPlayer != null && b){
            mediaPlayer.seekTo(progress);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(progress));
            textViewTempoAtual.setText(DataUtils.toStringSomenteHoras(calendar, 1));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("MEDIACONTROLLER", "onPrepared");
//        mediaController.setMediaPlayer(this);
//        mediaController.setAnchorView(view.findViewById(R.id.seekBarDuracao));
//        mediaController.setEnabled(true);
//        mediaController.show();

//        handler.post(new Runnable() {
//            public void run() {
//                mediaController.setEnabled(true);
//                mediaController.show();
//            }
//        });
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.seekTo(0);
        //mediaPlayer.stop();
        isPlaying = false;
        isPaused = false;
        btnPlay.setImageResource(R.drawable.ic_play);
//        handler.removeCallbacks(runnable);
//        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            isPlaying = false;
            isPaused = false;
        }

        if(mediaPlayer != null){
            mediaPlayer.release();
        }

        super.onDismiss(dialog);
    }

    //-------------------------------------------------------------------------------
}
