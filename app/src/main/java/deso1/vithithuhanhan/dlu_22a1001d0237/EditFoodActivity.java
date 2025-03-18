package deso1.vithithuhanhan.dlu_22a1001d0237;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditFoodActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText edtName, edtPrice;
    private Button btnUpdate, btnSelectImage;
    private ImageView imgPreview;
    private DbHelper dbHelper;
    private byte[] foodImage;
    private int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        edtName = findViewById(R.id.edt_name);
        edtPrice = findViewById(R.id.edt_price);
        btnUpdate = findViewById(R.id.btn_update);
        btnSelectImage = findViewById(R.id.btn_select_image);
        imgPreview = findViewById(R.id.img_preview);
        dbHelper = new DbHelper(this);

        // Nhận dữ liệu từ Intent
        foodId = getIntent().getIntExtra("food_id", -1);
        loadFoodData();

        btnSelectImage.setOnClickListener(v -> openImageChooser());

        btnUpdate.setOnClickListener(v -> updateFood());
    }

    private void loadFoodData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("Food", new String[]{"name", "price", "image"}, "id=?", new String[]{String.valueOf(foodId)}, null, null, null);

        if (cursor.moveToFirst()) {
            edtName.setText(cursor.getString(0));
            edtPrice.setText(cursor.getString(1));
            foodImage = cursor.getBlob(2);
            if (foodImage != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
                imgPreview.setImageBitmap(bitmap);
            }
        }
        cursor.close();
        db.close();
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgPreview.setImageBitmap(bitmap);
                foodImage = convertBitmapToByteArray(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void updateFood() {
        String name = edtName.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty() || foodImage == null) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin và chọn ảnh!", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("image", foodImage);

        int result = db.update("Food", values, "id=?", new String[]{String.valueOf(foodId)});
        db.close();

        if (result > 0) {
            Toast.makeText(this, "Cập nhật món ăn thành công!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Cập nhật món ăn thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
