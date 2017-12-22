package com.piccante.anurr.piccantemenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.roughike.bottombar.BottomBar;

/**
 * Created by anurr on 10/13/2017.
 */

public class InputName extends Fragment {

    CardView submitButton;
    MaterialEditText nameInput;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.input_name, container, false);

        submitButton = v.findViewById(R.id.submitbutton);
        nameInput = v.findViewById(R.id.nameinput);
        final BottomBar bottomBar = getActivity().findViewById(R.id.bottomBar);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameInput.length() == 0){
                    //Toast.makeText(getActivity(), "",Toast.LENGTH_LONG).show();
                    //bottomBar.selectTabAtPosition(2,true);
                    toOrder();
                } else {
                    toOrder();
            }}
        });

        return v;
    }

    private void toOrder(){

        Fragment order= new Order();
        String guest = String.valueOf(nameInput.getText());
        Bundle b = new Bundle();
        b.putString("guest", guest);
        order.setArguments(b);
        getFragmentManager().beginTransaction().
                setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out).
                replace(R.id.fragment_container, order).
                commit();

    }

}
