package com.zweckinger.myvaccreg.model;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Converters {

    private static Gson gson = new Gson();

    @TypeConverter
    public static Calendar toCalendar(Long l) {
        if(l != null) {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(l);
            return c;
        }
        return null;
    }

    @TypeConverter
    public static Long fromCalendar(Calendar c){
        return c == null ? null : c.getTime().getTime();
    }

    @TypeConverter
    public static List<Integer> fromBasicImmunization(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Integer>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String toBasicImmunization(List<Integer> someObjects) {
        return gson.toJson(someObjects);
    }
}