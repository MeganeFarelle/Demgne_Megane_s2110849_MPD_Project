package com.example.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather.databinding.WeatherItemLayoutBinding;
import com.example.weather.interfaces.WeatherItemsAdapterOnClickListener;
import com.example.weather.models.ForecastItem;

import java.util.ArrayList;

public class WeatherItemsAdapter extends RecyclerView.Adapter<WeatherItemsAdapter.ViewHolder> {

    private ArrayList<ForecastItem> dataList = new ArrayList<>();
    private ArrayList<ForecastItem> filterData = new ArrayList<>();
    private WeatherItemsAdapterOnClickListener onClickListener;
    private final Context context;

    WeatherItemLayoutBinding binding;

    public WeatherItemsAdapter(Context context, ArrayList<ForecastItem> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);

        this.filterData.clear();
        this.filterData.addAll(dataList);

        this.context = context;
    }

    public void resetData(ArrayList<ForecastItem> data){
        dataList.clear();
        dataList.addAll(data);

        filterData.clear();
        filterData.addAll(data);

        notifyDataSetChanged();
    }

    public void setOnClickListener(WeatherItemsAdapterOnClickListener listener){
        this.onClickListener = listener;
    }

    public void filterData(String query) {

        filterData.clear();
        for (ForecastItem item : dataList) {
            if (item.location.toLowerCase().contains(query.toLowerCase())) {
                filterData.add(item);
            }
        }

        if (filterData.size() == 0){
            filterData.addAll(dataList);
        }

        // Create a new adapter with the filtered data
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = WeatherItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForecastItem item = filterData.get(position);
        holder.bind(item);
        holder.binding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.view.callOnClick();
                if(onClickListener != null){
                    onClickListener.onItemClicked(filterData.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        WeatherItemLayoutBinding binding;

        public ViewHolder(@NonNull WeatherItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ForecastItem item){
            binding.setForecastItem(item);
            binding.executePendingBindings();
        }
    }
}
