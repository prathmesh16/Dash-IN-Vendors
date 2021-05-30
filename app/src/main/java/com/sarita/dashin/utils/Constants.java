package com.sarita.dashin.utils;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.sarita.dashin.models.ModelMess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

    public static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public static final FirebaseStorage mStorage = FirebaseStorage.getInstance();

    public static ModelMess CurrentUser = new ModelMess();

    public static List<Uri> MessImages = new ArrayList<>();
    public static List<String> MessImagenames = new ArrayList<>();
    public static Map<String, String> from = new HashMap<>();
    public static Map<String, String> to = new HashMap<>();

    public static final String LUNCH="lunch";
    public static final String DINNER = "dinner";
    public static final String BREAKFAST= "breakfast";


}
