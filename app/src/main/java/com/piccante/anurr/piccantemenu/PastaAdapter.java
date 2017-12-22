package com.piccante.anurr.piccantemenu;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.piccante.anurr.piccantemenu.databinding.PastaListViewBinding;

import java.util.ArrayList;

/**
 * Created by Obaro on 19/09/2016.
 */
public class PastaAdapter extends RecyclerView.Adapter<PastaAdapter.PastaViewHolder> {

    private ArrayList<PastaHelper> pasta = new ArrayList<>();
    Context mContext;
    PastaListViewBinding pastaBinding;

    public PastaAdapter(Context context) {
        mContext = context;
    }

    @Override
    public PastaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.pasta_list_view, parent, false);
        PastaViewHolder pastaViewHolder = new PastaViewHolder(view);
        return pastaViewHolder;
    }

    @Override
    public void onBindViewHolder(PastaViewHolder holder, int position) {
        int id = PastaHelper.getId();
        int pasta = PastaHelper.getImage();
        int sauce = PastaHelper.getSauce();
        int level = PastaHelper.getlevel();
        int amount = PastaHelper.getAmount();
        String toppings = PastaHelper.getToppings();
        int pastaprice = PastaHelper.getHarga();

        String pastatext = "";
        String saucetext = "";
        String leveltext;

        switch (pasta){
            case 0:
                holder.pastaBinding.typeimage.setImageResource(R.drawable.spaghettinon);
                pastatext = "Spaghetti";
                break;

            case 1:
                holder.pastaBinding.typeimage.setImageResource(R.drawable.pennenon);
                pastatext = "Penne";
                break;

            case 2:
                holder.pastaBinding.typeimage.setImageResource(R.drawable.fusilinon);
                pastatext = "Fusilli";
                break;
        }

        switch (sauce){
            case 0: saucetext = "Blackpeper";
                break;

            case 1: saucetext = "Bolognese";
                break;

            case 2: saucetext = "Carbonara";
                break;
        }

        if (level == 0) {
            leveltext = "<i>CALM</i>";
        } else {
            leveltext = String.valueOf(level);
        }

        holder.pastaBinding.amount.setText("Amount: " + amount);
        holder.pastaBinding.pastaTitle.setText(Html.fromHtml(pastatext + " ft. " + saucetext + " Lvl. " + leveltext));
        if (toppings.length() == 0) {
            holder.pastaBinding.pastaToppings.setText("WITHOUT TOPPINGS");
        } else {
            holder.pastaBinding.pastaToppings.setText("WITH TOPPINGS\n   "+toppings);
        }

        holder.pastaBinding.price.setText("IDR "+pastaprice);
    }

    @Override
    public int getItemCount() {
        return pasta.size();
    }

    public static class PastaViewHolder extends RecyclerView.ViewHolder {
        private final PastaListViewBinding pastaBinding;
        LinearLayout viewForeground;
        RelativeLayout viewBackground;
        private Context mContext;

        public PastaViewHolder(View itemView) {
            super(itemView);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            pastaBinding = DataBindingUtil.bind(itemView);

        }
    }

    public void removeItemAtPosition(int position) {
        pasta.remove(position);
        notifyItemRemoved(position);
    }

    public void addItemAtPosition(int position, int pastas, int sauce, int level, String toppings, int amount, int harga) {
        PastaHelper mLog = new PastaHelper();
        mLog.setImage(pastas);
        mLog.setSauce(sauce);
        mLog.setLevel(level);
        mLog.setToppings(toppings);
        mLog.setAmount(amount);
        mLog.setHarga(harga);
        pasta.add(position, mLog);
        notifyItemInserted(position);
    }
}