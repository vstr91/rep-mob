package br.com.vostre.repertori.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import br.com.vostre.repertori.App;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.AnalyticsApplication;
import br.com.vostre.repertori.utils.CustomScrollView;

public class CifraFragment extends Fragment implements CustomScrollView.OnScrollChangedListener, View.OnTouchListener {

    TextView textViewNome;
    TextView textViewArtista;
    TextView textViewCifra;
    CustomScrollView scrollView;
    MusicaDBHelper musicaDBHelper;
    ObjectAnimator animator;
    Tracker mTracker;

    public CifraFragment() {
        // Required empty public constructor
    }


    public static CifraFragment newInstance() {
        CifraFragment fragment = new CifraFragment();
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
        View v = inflater.inflate(R.layout.fragment_cifra, container, false);

        textViewNome = (TextView) v.findViewById(R.id.textViewNome);
        textViewArtista = (TextView) v.findViewById(R.id.textViewArtista);
        textViewCifra = (TextView) v.findViewById(R.id.textViewCifra);
        scrollView = (CustomScrollView) v.findViewById(R.id.scrollView);

        musicaDBHelper = new MusicaDBHelper(getContext());
        Musica musica = new Musica();
        musica.setId(getArguments().getString("musica"));

        musica = musicaDBHelper.carregar(getContext(), musica);
        textViewNome.setText(musica.getNome());
        textViewArtista.setText(musica.getArtista().getNome());

        if(!musica.getCifra().equals("null") && !musica.getCifra().isEmpty()){
            textViewCifra.setText(musica.getCifra());
        } else{
            textViewCifra.setText("Cifra não cadastrada.");
        }



//        scrollView.setOnScrollChangedListener(this);
        scrollView.setOnTouchListener(this);
        scrollDown();

        return v;
    }

    @Override
    public void onResume() {
        mTracker.setScreenName("Tela Cifra");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
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
