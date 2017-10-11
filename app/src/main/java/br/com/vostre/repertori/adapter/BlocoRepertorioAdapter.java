/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.vostre.repertori.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import br.com.vostre.repertori.R;
import br.com.vostre.repertori.listener.BlocoButtonClickListener;
import br.com.vostre.repertori.listener.ButtonClickListener;
import br.com.vostre.repertori.model.BlocoRepertorio;
import br.com.vostre.repertori.model.Musica;

public class BlocoRepertorioAdapter extends ArrayAdapter<BlocoRepertorio> {

    final int INVALID_ID = -1;

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
    List<BlocoRepertorio> blocos;
    private Activity context;
    ImageButton btnExcluir;
    BlocoButtonClickListener listener;

    public BlocoRepertorioAdapter(Activity context, int textViewResourceId, List<BlocoRepertorio> objects) {
        super(context, textViewResourceId, objects);
        blocos = objects;
        this.context = context;

        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i).getId(), i);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final BlocoRepertorio blocoRepertorio = blocos.get(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_blocos_repertorio, null, true);

        TextView textViewCont = (TextView) rowView.findViewById(R.id.textViewCont);
        TextView textViewNome = (TextView) rowView.findViewById(R.id.textViewNome);
        btnExcluir = (ImageButton) rowView.findViewById(R.id.btnExcluirBloco);

        textViewCont.setText(String.valueOf(position+1));

        textViewNome.setText(blocoRepertorio.getNome());

        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onButtonClicked(v, blocoRepertorio);
            }
        });

        btnExcluir.setFocusable(false);

        return rowView;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        BlocoRepertorio item = getItem(position);
        return mIdMap.get(item.getId());
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public BlocoButtonClickListener getListener() {
        return listener;
    }

    public void setListener(BlocoButtonClickListener listener) {
        this.listener = listener;
    }
}
