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
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import br.com.vostre.repertori.ArtistaDetalheActivity;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.adapter.ArtistaList;
import br.com.vostre.repertori.form.ModalCadastroArtista;
import br.com.vostre.repertori.listener.ModalCadastroListener;
import br.com.vostre.repertori.model.Artista;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.ArtistaDBHelper;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.CustomScrollView;

public class LetraFragment extends Fragment implements CustomScrollView.OnScrollChangedListener, View.OnTouchListener {

    TextView textViewNome;
    TextView textViewArtista;
    TextView textViewLetra;
    CustomScrollView scrollView;
    MusicaDBHelper musicaDBHelper;
    ObjectAnimator animator;

    public LetraFragment() {
        // Required empty public constructor
    }


    public static LetraFragment newInstance() {
        LetraFragment fragment = new LetraFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        musicaDBHelper = new MusicaDBHelper(getContext());
        Musica musica = new Musica();
        musica.setId(getArguments().getString("musica"));

        musica = musicaDBHelper.carregar(getContext(), musica);
        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());
        textViewLetra.setText(musica.getLetra());

//        scrollView.setOnScrollChangedListener(this);
        scrollView.setOnTouchListener(this);
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
        scrollView.post(new Runnable() {

            public void run() {

                if(scrollView.getScrollY() != scrollView.getBottom()){
                    animator = ObjectAnimator.ofInt(scrollView, "ScrollY", scrollView.getBottom());
                    animator.setDuration(50000);
                    animator.start();
                } else{
                    animator.cancel();
                }




            }

        });
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

}
