package br.com.vostre.repertori.form;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.TempoMusicaEvento;
import br.com.vostre.repertori.utils.Constants;
import br.com.vostre.repertori.utils.GetAudioTask;

public class ModalPlayer extends android.support.v4.app.DialogFragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    Button btnFechar;
    Button btnPlay;
    SeekBar seekBarDuracao;
    TextView textViewMusica;
    TempoMusicaEvento tme;

    boolean isPlaying = false;
    MediaPlayer mediaPlayer;

    public TempoMusicaEvento getTme() {
        return tme;
    }

    public void setTme(TempoMusicaEvento tme) {
        this.tme = tme;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modal_player, container, false);

        view.setMinimumWidth(700);

        btnFechar = (Button) view.findViewById(R.id.btnFechar);
        btnPlay = (Button) view.findViewById(R.id.btnPlay);
        textViewMusica = (TextView) view.findViewById(R.id.textViewMusica);
        seekBarDuracao = (SeekBar) view.findViewById(R.id.seekBarDuracao);

//        textViewMusica.setText(tme.getMusicaEvento().getMusica().getNome());

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
                        mediaPlayer.stop();
                        isPlaying = false;
                    }

                } else{
                    File f  = new File(Constants.CAMINHO_PADRAO_AUDIO+File.separator+tme.getAudio());

                    if(f.exists() && f.canExecute()){
                        mediaPlayer = new MediaPlayer();

                        try {
                            mediaPlayer.setDataSource(f.getAbsolutePath());
                            seekBarDuracao.setMax(mediaPlayer.getDuration());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            isPlaying = true;

                            final Handler mHandler = new Handler();
                            //Make sure you update Seekbar on UI thread
                            this.getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    if(mediaPlayer != null){
                                        int mCurrentPosition = mediaPlayer.getCurrentPosition() / 1000;
                                        seekBarDuracao.setProgress(mCurrentPosition);
                                    }
                                    mHandler.postDelayed(this, 1000);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else{
                        Toast.makeText(getContext(), "Áudio não encontrado. Pode ter sido renomeado ou movido.", Toast.LENGTH_SHORT).show();
                    }

                }

                break;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if(mediaPlayer != null && b){
            mediaPlayer.seekTo(progress * 1000);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
