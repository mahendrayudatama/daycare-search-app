package com.example.kidcare.Model;

import java.util.Comparator;

public class ModelGrowthList {
String age;
    String date;
    String height;
    String weight;
    String key;

    public String getKey() {
        return key;
    }


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

    public ModelGrowthList(String age, String date, String height, String weight, String key) {
        this.age = age;
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.key = key;
    }
    public static Comparator<ModelGrowthList> dateComparatorZA = new Comparator<ModelGrowthList>() {
        @Override
        public int compare(ModelGrowthList t1, ModelGrowthList t2) {
            return t2.getDate().compareTo(t1.getDate());
        }
    };
    public static Comparator<ModelGrowthList> dateComparatorAZ = new Comparator<ModelGrowthList>() {
        @Override
        public int compare(ModelGrowthList t1, ModelGrowthList t2) {
            return t1.getDate().compareTo(t2.getDate());
        }
    };
}

