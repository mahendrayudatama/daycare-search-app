package com.example.kidcare.Model;

import android.net.Uri;

import java.net.URI;
import java.util.Comparator;

public class ModelSearch {
    String daycareName;
    String daycarePrice;
    String daycareDistance;
    String daycarePic;

    String description;
    String facilities;
    String phoneNumber;
    String address;
    String email;
    String latitude;
    String longitude;
    String quota;
    String currentQuota;
    String mediaUrl;

    public String getDescription() {
        return description;
    }

    public String getFacilities() {
        return facilities;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getQuota() {
        return quota;
    }

    public String getCurrentQuota() {
        return currentQuota;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getDaycarePic() {
        return daycarePic;
    }

    public String getDaycareName() {
        return daycareName;
    }

    public String getDaycarePrice() {
        return daycarePrice;
    }

    public String getDaycareDistance() {
        return daycareDistance;
    }

    public ModelSearch(String daycareName,
                       String daycarePrice,
                       String daycareDistance,
                       String daycarePic,
                       String description,
                       String facilities,
                       String phoneNumber,
                       String address,
                       String email,
                       String latitude,
                       String longitude,
                       String quota,
                       String currentQuota,
                       String mediaUrl) {
        this.daycareName = daycareName;
        this.daycarePrice = daycarePrice;
        this.daycareDistance = daycareDistance;
        this.daycarePic = daycarePic;
        this.description = description;
        this.facilities = facilities;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.quota = quota;
        this.currentQuota = currentQuota;
        this.mediaUrl = mediaUrl;
    }

    public static Comparator<ModelSearch> daycareAZComparator = new Comparator<ModelSearch>() {
        @Override
        public int compare(ModelSearch d1, ModelSearch d2) {
            if (Double.parseDouble(d1.getDaycarePrice().replace(".", "")) < Double.parseDouble(d2.getDaycarePrice().replace(".", ""))) {
                return -1;
            }
            if (Double.parseDouble(d1.getDaycarePrice().replace(".", "")) > Double.parseDouble(d2.getDaycarePrice().replace(".", ""))) {
                return 1;
            }
            return 0;
//            return d1.getDaycarePrice().replace(".","").compareTo(d2.getDaycarePrice().replace(".",""));
        }
    };
    public static Comparator<ModelSearch> daycareZAComparator = new Comparator<ModelSearch>() {
        @Override
        public int compare(ModelSearch d1, ModelSearch d2) {
            if (Double.parseDouble(d1.getDaycarePrice().replace(".", "")) < Double.parseDouble(d2.getDaycarePrice().replace(".", ""))) {
                return 1;
            }
            if (Double.parseDouble(d1.getDaycarePrice().replace(".", "")) > Double.parseDouble(d2.getDaycarePrice().replace(".", ""))) {
                return -1;
            }
            return 0;
//            return d2.getDaycarePrice().replace(".","").compareTo(d1.getDaycarePrice().replace(".",""));
        }
    };
    public static Comparator<ModelSearch> daycareDistanceAscendingComparator = new Comparator<ModelSearch>() {
        @Override
        public int compare(ModelSearch d1, ModelSearch d2) {
            if (Double.parseDouble(d1.getDaycareDistance().replace(",", "")) < Double.parseDouble(d2.getDaycareDistance().replace(",", ""))) {
                return -1;
            }
            if (Double.parseDouble(d1.getDaycareDistance().replace(",", "")) > Double.parseDouble(d2.getDaycareDistance().replace(",", ""))) {
                return 1;
            }
            return 0;
        }
    };
    public static Comparator<ModelSearch> daycareDistanceDescendingComparator = new Comparator<ModelSearch>() {
        @Override
        public int compare(ModelSearch d1, ModelSearch d2) {
            if (Double.parseDouble(d1.getDaycareDistance().replace(",", "")) > Double.parseDouble(d2.getDaycareDistance().replace(",", ""))) {
                return -1;
            }
            if (Double.parseDouble(d1.getDaycareDistance().replace(",", "")) < Double.parseDouble(d2.getDaycareDistance().replace(",", ""))) {
                return 1;
            }
            return 0;
        }
    };
}
