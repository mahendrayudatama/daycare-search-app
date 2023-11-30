package com.example.kidcare.Model;

import java.util.Comparator;

public class ModelGrowth {
String age,date,height,weight;

    public String getAge() {
        return age;
    }

    public String getDate() {
        return date;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public ModelGrowth(String age, String date, String height, String weight) {
        this.age = age;
        this.date = date;
        this.height = height;
        this.weight = weight;
    }
    public static Comparator<ModelGrowth> dateComparatorZA = new Comparator<ModelGrowth>() {
        @Override
        public int compare(ModelGrowth t1, ModelGrowth t2) {
            return t2.getDate().compareTo(t1.getDate());
        }
    };
    public static Comparator<ModelGrowth> dateComparatorAZ = new Comparator<ModelGrowth>() {
        @Override
        public int compare(ModelGrowth t1, ModelGrowth t2) {
            return t1.getDate().compareTo(t2.getDate());
        }
    };
}

