package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;


public class Emojifier {
    private static final String TAG = Emojifier.class.getSimpleName();

    protected static void detectFaces(Context context, Bitmap bitmap) {

        FaceDetector faceDetector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .build();

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<Face> faces = faceDetector.detect(frame);

        String message = "Number of faces detected: ";
        Log.i(TAG, message + faces.size());
        Toast.makeText(context, message + faces.size(), Toast.LENGTH_SHORT).show();

        faceDetector.release();
    }
}
