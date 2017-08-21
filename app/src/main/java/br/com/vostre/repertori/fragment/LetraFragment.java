package br.com.vostre.repertori.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;

import java.util.List;

import br.com.vostre.repertori.App;
import br.com.vostre.repertori.ArtistaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.form.ModalCadastroArtista;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.AnalyticsApplication;
import br.com.vostre.repertori.utils.CustomScrollView;

public class LetraFragment extends Fragment implements CustomScrollView.OnScrollChangedListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener {

    TextView textViewNome;
    TextView textViewArtista;
    TextView textViewLetra;
    CustomScrollView scrollView;
    MusicaDBHelper musicaDBHelper;
    ObjectAnimator animator;

    Tracker mTracker;

    SeekBar seekBarVelocidade;
    int velocidade = 0;

    LinearLayout linearLayout;

    public LetraFragment() {
        // Required empty public constructor
    }


    public static LetraFragment newInstance() {
        LetraFragment fragment = new LetraFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App application = (App) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_letra, container, false);

        textViewNome = (TextView) v.findViewById(R.id.textViewNome);
        textViewArtista = (TextView) v.findViewById(R.id.textViewArtista);
        textViewLetra = (TextView) v.findViewById(R.id.textViewLetra);
        scrollView = (CustomScrollView) v.findViewById(R.id.scrollView);
        seekBarVelocidade = (SeekBar) v.findViewById(R.id.seekBarVelocidade);

        linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);

        musicaDBHelper = new MusicaDBHelper(getContext());
        Musica musica = new Musica();
        musica.setId(getArguments().getString("musica"));

        musica = musicaDBHelper.carregar(getContext(), musica);
        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());

        if(!musica.getLetra().equals("null") && !musica.getLetra().isEmpty()){
            textViewLetra.setText(musica.getLetra());
        } else{
            textViewLetra.setText("Letra não cadastrada.");
        }

//        scrollView.setOnScrollChangedListener(this);
        scrollView.setOnTouchListener(this);
        //scrollView.setScrollY(0);

        seekBarVelocidade.setOnSeekBarChangeListener(this);

        scrollDown();

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();

        if(animator != null){
            animator.cancel();
        }

    }

    private void scrollDown(){

        if(velocidade > 0 && velocidade < 50000){
            scrollView.post(new Runnable() {

                public void run() {

                    int diff = (scrollView.getChildAt(0).getHeight()-(scrollView.getHeight()+scrollView.getScrollY()));

                    System.out.println("Diff: "+diff+" | "+scrollView.getBottom()+" | "+scrollView.getChildAt(0).getHeight()+" | "+velocidade);

                    if(diff > 0){
                        animator = ObjectAnimator.ofInt(scrollView, "ScrollY", scrollView.getChildAt(0).getHeight());
                        animator.setDuration(velocidade);
                        animator.start();
                    } else{

                        if(animator != null){
                            animator.cancel();
                        }

                    }




                }

            });
        }

    }

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

            if(animator != null){
                animator.cancel();
            }

        } else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
            scrollDown();
        }

        //scrollDown();

        return false;

    }

    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        if (visible && isResumed()){
            scrollView.setScrollY(0);
            scrollDown();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        velocidade = (100000 - i * 1000) / 2;

        if(animator != null){
            animator.cancel();
        }

        scrollDown();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
