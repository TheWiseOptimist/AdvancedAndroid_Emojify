/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.emojify;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

class Emojifier {

    private static final String LOG_TAG = Emojifier.class.getSimpleName();
    private static final float EYE_OPEN_THRESHOLD = 0.5f;
    private static final float SMILING_THRESHOLD = 0.1f;
    private static boolean is_right_eye_open = true;
    private static boolean is_left_eye_open = true;
    private static boolean is_smiling = true;
    private static Emoji emoji;

    /**
     * Method for detecting faces in a bitmap.
     *
     * @param context The application context.
     * @param picture The picture in which to detect the faces.
     */
    static void detectFaces(Context context, Bitmap picture) {

        // Create the face detector, disable tracking and enable classifications
        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        // Build the frame
        Frame frame = new Frame.Builder().setBitmap(picture).build();

        // Detect the faces
        SparseArray<Face> faces = detector.detect(frame);

        // Log the number of faces
        Log.d(LOG_TAG, "detectFaces: number of faces = " + faces.size());

        // If there are no faces detected, show a Toast message
        if (faces.size() == 0) {
            Toast.makeText(context, R.string.no_faces_message, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < faces.size(); ++i) {
                Face face = faces.valueAt(i);

                // Log the classification probabilities for each face.
                whichEmoji(face);
                // TODO completed (6): Change the call to getClassifications() to whichEmoji() to log the appropriate emoji for the facial expression.
                Toast.makeText(context, "s: " + is_smiling
                        + " r: " + is_right_eye_open
                        + " l: " + is_left_eye_open, Toast.LENGTH_SHORT).show();
            }

        }
        Toast.makeText(context, "# faces: " + faces.size()
                , Toast.LENGTH_LONG).show();

        // Release the detector
        detector.release();
    }


    /**
     * Method for logging the classification probabilities.
     *
     * @param face The face to get the classification probabilities.
     */
    private static void whichEmoji(Face face) {
        // TODO completed (2): Change the name of the getClassifications() method to whichEmoji() (also change the log statements)
        // Log all the probabilities
        float smilingProbability = face.getIsSmilingProbability();
        float leftEyeOpenProbability = face.getIsLeftEyeOpenProbability();
        float rightEyeOpenProbability = face.getIsRightEyeOpenProbability();

        Log.d(LOG_TAG, "whichEmoji: smilingProb = " + smilingProbability);
        Log.d(LOG_TAG, "whichEmoji: leftEyeOpenProb = "
                + leftEyeOpenProbability);
        Log.d(LOG_TAG, "whichEmoji: rightEyeOpenProb = "
                + rightEyeOpenProbability);

        is_smiling = smilingProbability > SMILING_THRESHOLD;
        is_right_eye_open = rightEyeOpenProbability > EYE_OPEN_THRESHOLD;
        is_left_eye_open = leftEyeOpenProbability > EYE_OPEN_THRESHOLD;

        // TODO completed (3): Create threshold constants for a person smiling, and and eye being open by
        // taking pictures of yourself and your friends and noting the logs.
        // TODO completed (4): Create 3 boolean variables to track the state of the facial expression based
        // on the thresholds you set in the previous step: smiling, left eye closed, right eye closed.
        // TODO completed (5): Create an if/else system that selects the appropriate emoji based on the above booleans and log the result.
        if (is_smiling) {
            if (is_right_eye_open && is_left_eye_open) emoji = Emoji.OPEN_SMILING;
            else if (is_right_eye_open) emoji = Emoji.RIGHT_SMILING;
            else if (is_left_eye_open) emoji = Emoji.LEFT_SMILING;
            else emoji = Emoji.CLOSED_SMILING;
        } else {
            if (is_right_eye_open && is_left_eye_open) emoji = Emoji.OPEN_FROWNING;
            else if (is_right_eye_open) emoji = Emoji.RIGHT_FROWNING;
            else if (is_left_eye_open) emoji = Emoji.LEFT_FROWNING;
            else emoji = Emoji.CLOSED_FROWNING;
        }

//        if (is_smiling) {
//            if (is_right_eye_open) {
//                if (is_left_eye_open) emoji = Emoji.OPEN_SMILING;
//                else emoji = Emoji.RIGHT_SMILING;
//            } else if (is_left_eye_open) emoji = Emoji.LEFT_SMILING;
//            else emoji = Emoji.CLOSED_SMILING;
//        } else {
//            if (is_right_eye_open) {
//                if (is_left_eye_open) emoji = Emoji.OPEN_FROWNING;
//                else emoji = Emoji.RIGHT_FROWNING;
//            } else if (is_left_eye_open) emoji = Emoji.LEFT_FROWNING;
//            else emoji = Emoji.CLOSED_FROWNING;
//        }

        Log.d(LOG_TAG, emoji.toString());
    }

    // TODO completed (1): Create an enum class called Emoji that contains all the possible emoji you can make (smiling, frowning, left wink, right wink, left wink frowning, right wink frowning, closed eye smiling, close eye frowning).
    public enum Emoji {
        OPEN_SMILING,
        OPEN_FROWNING,
        LEFT_SMILING,
        LEFT_FROWNING,
        RIGHT_SMILING,
        RIGHT_FROWNING,
        CLOSED_SMILING,
        CLOSED_FROWNING
    }
}
