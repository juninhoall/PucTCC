package com.pucpr.puctcc.dao;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthFirebase {
    private static DatabaseReference refFirebase;
    private static FirebaseAuth authFirebase;


    public static DatabaseReference getFirebase(){
        refFirebase = (refFirebase == null) ?FirebaseDatabase.getInstance().getReference() : refFirebase;
        return refFirebase;
    }
    public static FirebaseAuth getAuthFirebase(){
        authFirebase = (authFirebase == null) ?  FirebaseAuth.getInstance(): authFirebase;
        return authFirebase;
    }
}
