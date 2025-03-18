package deso1.vithithuhanhan.dlu_22a1001d0237;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private Context context;
    private List<Food> foodList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Food food);
    }

    public FoodAdapter(Context context, List<Food> foodList, OnItemClickListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.name.setText(food.getName());
        holder.price.setText("Giá: " + food.getPrice() + " đồng");

        // Lấy ảnh từ drawable dựa trên tên ảnh trong Food object
        int imageResId = context.getResources().getIdentifier(food.getImage(), "drawable", context.getPackageName());

        if (imageResId != 0) {
            holder.image.setImageResource(imageResId);
        } else {
            holder.image.setImageResource(R.drawable.ic_launcher_foreground); // Ảnh mặc định nếu không tìm thấy
        }

        holder.edit.setOnClickListener(v -> listener.onEditClick(food));
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        ImageView image, edit;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.food_name);
            price = itemView.findViewById(R.id.food_price);
            image = itemView.findViewById(R.id.food_image);  // Thêm ImageView cho ảnh món ăn
            edit = itemView.findViewById(R.id.food_edit);
        }
    }

}
