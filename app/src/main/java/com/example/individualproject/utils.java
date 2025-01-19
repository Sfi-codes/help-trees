package com.example.individualproject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class utils {

    public static  String currentUserID() {
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedin(){
        return currentUserID() != null;
    }
    public static DocumentReference currentUserDetails(){
return FirebaseFirestore.getInstance().collection("Users").document(currentUserID());
    }

}
