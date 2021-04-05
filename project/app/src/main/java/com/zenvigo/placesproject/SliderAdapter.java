package com.project.placesproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.pritam.customerportal.R;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;
//https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwia0Z2bivrmAhWgILcAHSJWB4sQjRx6BAgBEAQ&url=https%3A%2F%2Fcoupons-for-uber-couponkingsmen.en.aptoide.com%2F&psig=AOvVaw2I7AfDfdqEsomxGSmO8L38&ust=1578781578180881
public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private int mCount;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        //viewHolder.textViewDescription.setText("This is slider item " + position);

        switch (position) {
            case 0:
                Glide.with(viewHolder.itemView)
                        .load("https://f0.pngfuel.com/png/802/45/t-shirt-sales-clothing-shopping-50-off-sale-50-text-overlay-png-clip-art-thumbnail.png")
                        .into(viewHolder.imageViewBackground);
                break;
            case 1:
                 Glide.with(viewHolder.itemView)
                         .load(R.drawable.groceryhdpic)
                         .into(viewHolder.imageViewBackground);
                 break;
            case 2:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.longtenngig)
                        .into(viewHolder.imageViewBackground);
                break;
            case 3:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.butcherhdpic)
                        .into(viewHolder.imageViewBackground);
                break;
            case 4:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.veghdpic)
                        .into(viewHolder.imageViewBackground);
                break;
            case 5:
                Glide.with(viewHolder.itemView)
                        .load(R.drawable.stationaryhdpic)
                        .into(viewHolder.imageViewBackground);
                break;
            case 6:
                Glide.with(viewHolder.itemView)
                        .load("data:image/jpeg;base64,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")
                        .into(viewHolder.imageViewBackground);
                break;

            default:
                Glide.with(viewHolder.itemView)
                        .load("https://f0.pngfuel.com/png/802/45/t-shirt-sales-clothing-shopping-50-off-sale-50-text-overlay-png-clip-art-thumbnail.png")
                        .into(viewHolder.imageViewBackground);
                break;

        }

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return 8;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}