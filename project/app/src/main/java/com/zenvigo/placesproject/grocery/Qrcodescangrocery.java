package com.project.placesproject.grocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.project.placesproject.PayAmount;
import com.project.placesproject.R;

//
public class Qrcodescangrocery extends AppCompatActivity {



    ImageView image;
    String text2Qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcodescangrocery);
        final String invoiceid = getIntent().getStringExtra("invoiceid");

       FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

       String CId = currentUser.getUid();

       Button reached = (Button) findViewById(R.id.reached);
       reached.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent intent = new Intent(getApplicationContext(), PayAmount.class);
               intent.putExtra("invoiceid",invoiceid);
               startActivity(intent);

           }
       });

//
//            text = (EditText) findViewById(R.id.text);
//            gen_btn = (Button) findViewById(R.id.gen_btn);
        image = (ImageView) findViewById(R.id.image);
//            gen_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    text2Qr = text.getText().toString().trim();
        text2Qr = CId;
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


    }
}

