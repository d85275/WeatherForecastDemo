package ch.protonmail.android.protonmailtest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ch.protonmail.android.protonmailtest.IClickedCallback;
import ch.protonmail.android.protonmailtest.viewmodels.MainViewModel;
import ch.protonmail.android.protonmailtest.R;
import ch.protonmail.android.protonmailtest.repo.WeatherData;

/**
 * Created by ProtonMail on 2/25/19.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.DayViewHolder> {

    private List<WeatherData> data = new ArrayList<>();
    private Context ctx;
    private MainViewModel viewModel;
    private IClickedCallback callback;

    public ForecastAdapter(Context ctx, MainViewModel viewModel, IClickedCallback callback) {
        this.ctx = ctx;
        this.viewModel = viewModel;
        this.callback = callback;
    }

    public void setData(List<WeatherData> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_forecast, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, final int position) {
        String title = "Day " + data.get(position).getDay() + ": " + data.get(position).getDes();
        holder.titleView.setText(title);
        Bitmap image = viewModel.getThumbImage(data.get(position).day);
        if (image != null) {
            holder.ivImage.setImageBitmap(image);
        } else {
            holder.ivImage.setImageBitmap(null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClicked(data.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;
        private ImageView ivImage;

        public DayViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            ivImage = itemView.findViewById(R.id.image);
        }
    }
}
