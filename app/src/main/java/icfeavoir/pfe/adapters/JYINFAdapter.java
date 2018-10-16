package icfeavoir.pfe.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import icfeavoir.pfe.R;
import icfeavoir.pfe.controller.JYINFActivity;
import icfeavoir.pfe.controller.MYJURActivity;
import icfeavoir.pfe.model.Jury;
import icfeavoir.pfe.model.Project;

public class JYINFAdapter extends RecyclerView.Adapter<JYINFAdapter.JYINFViewHolder>{
    private JYINFActivity activity;

    private List<Project> projects;

    public JYINFAdapter(JYINFActivity jyinfActivity){
        this.activity = jyinfActivity;
        setProjects(new ArrayList<Project>());
    }

    public void setProjects(List<Project> projects){
        this.projects = projects;
    }

    @Override
    public int getItemCount(){
        return projects.size();
    }

    @Override
    public JYINFAdapter.JYINFViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View jyinfView = LayoutInflater.from(parent.getContext()).inflate(R.layout.jyinf_card_layout, parent, false);
        return new JYINFAdapter.JYINFViewHolder(jyinfView);
    }

    @Override
    public void onBindViewHolder(@NonNull JYINFAdapter.JYINFViewHolder holder, int position) {
        final Project project = projects.get(position);
        holder.project_project_id.setText(project.getProjectId());
        holder.project_title.setText(project.getTitle());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.clickProjectCard(project);
            }
        });
    }

    class JYINFViewHolder extends RecyclerView.ViewHolder{

        private final View view;

        private final TextView project_project_id;
        private final TextView project_title;

        public JYINFViewHolder(View view){
            super(view);
            this.view = view;
            project_project_id = view.findViewById(R.id.project_id);
            project_title = view.findViewById(R.id.title);
        }
    }
}
