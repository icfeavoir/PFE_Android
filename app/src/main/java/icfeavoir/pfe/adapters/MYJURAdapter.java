package icfeavoir.pfe.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.R;
import icfeavoir.pfe.controller.MYJURActivity;
import icfeavoir.pfe.model.Jury;

public class MYJURAdapter extends RecyclerView.Adapter<MYJURAdapter.MYJURViewHolder> {
    private MYJURActivity activity;

    private List<Jury> juries;

    public MYJURAdapter(MYJURActivity myjurActivity){
        this.activity = myjurActivity;
        setJuries(new ArrayList<Jury>());
    }

    public void setJuries(List<Jury> juries){
        this.juries = juries;
    }

    @Override
    public int getItemCount(){
        return juries.size();
    }

    @Override
    public MYJURViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View myjurView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myjur_card_layout, parent, false);
        return new MYJURViewHolder(myjurView);
    }

    @Override
    public void onBindViewHolder(@NonNull MYJURViewHolder holder, int position) {
        final Jury jury = juries.get(position);
        holder.jury_jury_id.setText("Jury n°" + String.valueOf(jury.getJuryId()));
        holder.jury_date.setText(jury.getDate());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.clickJuryCard(jury);
            }
        });
    }

    class MYJURViewHolder extends RecyclerView.ViewHolder{

        private final View view;

        private final TextView jury_jury_id;
        private final TextView jury_date;

        public MYJURViewHolder(View view){
            super(view);
            this.view = view;
            jury_jury_id = view.findViewById(R.id.jury_id);
            jury_date = view.findViewById(R.id.date);
        }
    }
}
