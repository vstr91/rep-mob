package br.com.vostre.repertori.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import br.com.vostre.repertori.App;
import br.com.vostre.repertori.R;
import br.com.vostre.repertori.model.Musica;
import br.com.vostre.repertori.model.dao.MusicaDBHelper;
import br.com.vostre.repertori.utils.AnalyticsApplication;
import br.com.vostre.repertori.utils.CustomScrollView;

public class CifraFragment extends Fragment implements CustomScrollView.OnScrollChangedListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener {

    TextView textViewNome;
    TextView textViewArtista;
    TextView textViewCifra;
    CustomScrollView scrollView;
    MusicaDBHelper musicaDBHelper;
    ObjectAnimator animator;
    Tracker mTracker;

    SeekBar seekBarVelocidade;
    int velocidade = 0;

    LinearLayout linearLayout;

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

        seekBarVelocidade = (SeekBar) v.findViewById(R.id.seekBarVelocidade);

        linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);

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

        seekBarVelocidade.setOnSeekBarChangeListener(this);

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
