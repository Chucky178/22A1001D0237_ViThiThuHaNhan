package deso1.vithithuhanhan.dlu_22a1001d0237;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DbHelper dbHelper;
    private FoodAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btn_add);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Nếu lần đầu chạy, thêm dữ liệu mặc định vào database
        if (dbHelper.getFoodCount() == 0) {
            insertDefaultFoods();
        }

        loadFoods(); // Load danh sách món ăn từ SQLite

        btnAdd.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddFoodActivity.class)));
    }

    private void loadFoods() {
        List<Food> foodList = dbHelper.getAllFoods(); // Lấy dữ liệu từ database
        adapter = new FoodAdapter(this, foodList, food -> {
            Intent intent = new Intent(MainActivity.this, EditFoodActivity.class);
            intent.putExtra("food_id", food.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void insertDefaultFoods() {
        List<Food> foodList = new ArrayList<>();
        foodList.add(new Food(1, "Lẩu chay", 50000, "pic1"));
        foodList.add(new Food(2, "Humberger", 25000, "pic2"));
        foodList.add(new Food(3, "Bánh mỳ", 60000, "pic3"));
        foodList.add(new Food(4, "Xúc xích", 20000, "pic4"));

        for (Food food : foodList) {
            dbHelper.addFood(food);
        }
    }
}
