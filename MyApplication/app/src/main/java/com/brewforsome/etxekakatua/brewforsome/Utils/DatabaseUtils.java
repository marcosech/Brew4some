package com.brewforsome.etxekakatua.brewforsome.Utils;

import com.brewforsome.etxekakatua.brewforsome.model.UserDto;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by etxekakatua on 9/3/18.
 */

public class DatabaseUtils {

    public static UserDto userdto;

    public static void createNewBeer(String name, String style, String comments, String malt, String hop, String yeast, String extra, String interests, String abv, String location, String userId, FirebaseAuth mAuth) {


        DatabaseReference currentUserBeerCharacteristics = FirebaseDatabase.getInstance().getReference().child("Beers").child(userId).child(name);


        Map newBeer = new HashMap();
//            newBeer.put("number", "Beer #" + Integer.toString(count[0]));
        newBeer.put("name", name);
        newBeer.put("style", style);
        newBeer.put("comments", comments);
        newBeer.put("malt", malt);
        newBeer.put("hop", hop);
        newBeer.put("yeast", yeast);
        newBeer.put("abv", abv);
        newBeer.put("extra", extra);
        newBeer.put("interests", interests);
        newBeer.put("location", location);
        newBeer.put("userEmail", Application.email);
        newBeer.put("phone", Application.phone);
        newBeer.put("brewerName", Application.name);


        currentUserBeerCharacteristics.setValue(newBeer);

    }



}
