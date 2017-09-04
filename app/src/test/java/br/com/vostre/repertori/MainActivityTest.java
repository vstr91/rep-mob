package br.com.vostre.repertori;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import br.com.vostre.repertori.fragment.EventosFragment;

import static com.google.common.truth.Truth.assertThat;

/**
 * Created by Almir on 04/09/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "app/src/main/AndroidManifest.xml", emulateSdk = 17)
public class MainActivityTest {

    @Test
    public void shouldHaveEventData(){
        MainActivity activity = Robolectric.buildActivity(MainActivity.class).create().get();
        assertThat(activity.fragment).isEqualTo(EventosFragment.class);
    }

}
