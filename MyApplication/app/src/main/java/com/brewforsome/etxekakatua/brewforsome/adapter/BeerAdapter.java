package com.brewforsome.etxekakatua.brewforsome.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brewforsome.etxekakatua.brewforsome.R;
import com.brewforsome.etxekakatua.brewforsome.model.BeerDto;

import java.util.List;

/**
 * Created by etxekakatua on 5/1/18.
 */

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.ViewHolder>{

    public TextView nameView;
    private BeerDto beerItem;
    private View viewItem;
    List<BeerDto> MainImageUploadInfoList;
    Context context;


    public BeerAdapter(Context context, List<BeerDto> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_beer, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BeerDto beer = MainImageUploadInfoList.get(position);

        holder.beerNameTxt.setText(beer.getName());

        holder.beerStyleTxt.setText(beer.getStyle());

        holder.beerBrewerTxt.setText(beer.getBrewerName());

        holder.beerLocationTxt.setText(beer.getLocation());

        holder.beerInterestsTxt.setText(beer.getInterests());

    }



    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView beerNameTxt;
        public TextView beerStyleTxt;
        public TextView beerBrewerTxt;
        public TextView beerLocationTxt;
        public TextView beerInterestsTxt;

        public ViewHolder(View itemView) {

            super(itemView);

            beerNameTxt = (TextView) itemView.findViewById(R.id.elementBeerName);

            beerStyleTxt = (TextView) itemView.findViewById(R.id.elementBeerStyle);

            beerBrewerTxt = (TextView) itemView.findViewById(R.id.elementBeerBrewerName);

            beerLocationTxt = (TextView) itemView.findViewById(R.id.elementBeerLocation);

            beerInterestsTxt = (TextView) itemView.findViewById(R.id.elementBeerInterests);

            /*Shader textShader=new LinearGradient(0, 0, 60, 20,
                    new int[]{R.color.brewOrangeOne,R.color.brewOrangeTwo,R.color.brewPink},
                    new float[]{0, 1, 2}, Shader.TileMode.CLAMP);
            beerNameTxt.getPaint().setShader(textShader);*/

        }
    }
}
