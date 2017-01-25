package kagura.project.com.a8.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kagura.project.com.a8.R;
import kagura.project.com.a8.objects.Result;


public class ResultAdapter extends ArrayAdapter<Result> {

    private Activity activity;
    private List<Result> items;
    private int row;

    public ResultAdapter(Activity act, int resource, List<Result> arrayList) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.items = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        ViewHolder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);

            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if((items == null) || ((position + 1 ) > items.size()))
            return view;

        Result result = items.get(position);

        holder.name = (TextView) view.findViewById(R.id.name);
        holder.game = (TextView) view.findViewById(R.id.game);
        holder.level = (TextView) view.findViewById(R.id.level);
        holder.tries = (TextView) view.findViewById(R.id.tries);
        holder.time = (TextView) view.findViewById(R.id.time);
        holder.date = (TextView) view.findViewById(R.id.date);

        if (holder.name != null && null != result.getName()
                && result.getName().trim().length() > 0) {
            holder.name.setText(Html.fromHtml(result.getName()));
        }


        holder.game.setText(Html.fromHtml(result.getGame()));

        holder.level.setText(Html.fromHtml(Integer.toString(result.getLevel())));

        holder.tries.setText(Html.fromHtml(Integer.toString(result.getTries())));

        holder.time.setText(Html.fromHtml(Long.toString(result.getTime()) + " s"));

        holder.date.setText(Html.fromHtml(result.getDate()));


        return view;

    }

    private class ViewHolder {
        TextView name, game, level, tries, time, date;
    }
}
