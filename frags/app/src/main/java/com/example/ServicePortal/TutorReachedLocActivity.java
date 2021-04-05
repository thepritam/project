package com.example.ServicePortal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class TutorReachedLocActivity extends AppCompatActivity {

    EditText text;
    Button gen_btn;
    ImageView image;
    String text2Qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_reached_loc);

        Intent intent = getIntent();
        long Assigned = intent.getLongExtra("Assigned", 0);
        long Completed = intent.getLongExtra("Completed", 0);
        String Category = intent.getStringExtra("Category");
        String AssignedId = intent.getStringExtra("AssignedId");
        final String CId = intent.getStringExtra("CId");
        final String OrderId = intent.getStringExtra("OrderId");
        String Description = intent.getStringExtra("Description");
        double Latitude = intent.getDoubleExtra("Latitude", 0);
        double Longitude = intent.getDoubleExtra("Longitude", 0);
        String AudioDescription = intent.getStringExtra("AudioDescription");
        String Uid = intent.getStringExtra("Uid");
//
//            text = (EditText) findViewById(R.id.text);
//            gen_btn = (Button) findViewById(R.id.gen_btn);
        image = (ImageView) findViewById(R.id.image);
//            gen_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    text2Qr = text.getText().toString().trim();
        text2Qr = OrderId;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            image.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
//                }
//            });

//        intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("Assigned", Assigned);
//        intent.putExtra("Completed", Completed);
//        intent.putExtra("Category", Category);
//        intent.putExtra("AssignedId", AssignedId);
//        intent.putExtra("CId", CId);
//        intent.putExtra("Description", Description);
//        intent.putExtra("Latitude", Latitude);
//        intent.putExtra("Longitude", Longitude);
//        intent.putExtra("AudioDescription", AudioDescription);
//        intent.putExtra("Uid", Uid);
//        startActivity(intent);
    }
}
