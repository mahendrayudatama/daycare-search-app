package com.example.kidcare.Model;

import android.net.Uri;

import java.util.Comparator;

public class ModelDaycare {
    String daycareName;
    String description;
    String facilities;
    String phoneNumber;
    String price;
    String address;
    String email;
    String latitude;
    String longitude;
    String quota;
    String currentQuota;
    String profilePic;
    String mediaUrl;


    public ModelDaycare(String daycareName, String description, String facilities, String phoneNumber, String price, String address, String email, String latitude, String longitude, String quota, String currentQuota, String mediaUrl, String profilePic) {
        this.daycareName = daycareName;
        this.description = description;
        this.facilities = facilities;
        this.phoneNumber = phoneNumber;
        this.price = price;
        this.address = address;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.quota = quota;
        this.currentQuota = currentQuota;
        this.profilePic = profilePic;
        this.mediaUrl = mediaUrl;
    }


    public String getProfilePic() {
        return profilePic;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getDaycareName() {
        return daycareName;
    }

    public String getDescription() {
        return description;
    }

    public String getFacilities() {
        return facilities;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getQuota() {
        return quota;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCurrentQuota() {
        return currentQuota;
    }

    public static Comparator<ModelDaycare> daycareAZComparator = new Comparator<ModelDaycare>() {
        @Override
        public int compare(ModelDaycare d1, ModelDaycare d2) {
            if (Double.parseDouble(d1.getPrice().replace(".","")) < Double.parseDouble(d2.getPrice().replace(".",""))) {
                return -1;
            }
            if (Double.parseDouble(d1.getPrice().replace(".","")) > Double.parseDouble(d2.getPrice().replace(".",""))) {
                return 1;
            }
            return 0;
//            return d1.getDaycarePrice().replace(".","").compareTo(d2.getDaycarePrice().replace(".",""));
        }
    };
    public static Comparator<ModelDaycare> daycareZAComparator = new Comparator<ModelDaycare>() {
        @Override
        public int compare(ModelDaycare d1, ModelDaycare d2) {
            if (Double.parseDouble(d1.getPrice().replace(".","")) < Double.parseDouble(d2.getPrice().replace(".",""))) {
                return 1;
            }
            if (Double.parseDouble(d1.getPrice().replace(".","")) > Double.parseDouble(d2.getPrice().replace(".",""))) {
                return -1;
            }
            return 0;
//            return d2.getDaycarePrice().replace(".","").compareTo(d1.getDaycarePrice().replace(".",""));
        }
    };
    public static Comparator<ModelSearch> daycareDistanceAscendingComparator = new Comparator<ModelSearch>() {
        @Override
        public int compare(ModelSearch d1, ModelSearch d2) {
            if (Double.parseDouble(d1.getDaycareDistance().replace(",","")) < Double.parseDouble(d2.getDaycareDistance().replace(",",""))) {
                return -1;
            }
            if (Double.parseDouble(d1.getDaycareDistance().replace(",","")) > Double.parseDouble(d2.getDaycareDistance().replace(",",""))) {
                return 1;
            }
            return 0;
        }
    };
    public static Comparator<ModelSearch> daycareDistanceDescendingComparator = new Comparator<ModelSearch>() {
        @Override
        public int compare(ModelSearch d1, ModelSearch d2) {
            if (Double.parseDouble(d1.getDaycareDistance().replace(",","")) > Double.parseDouble(d2.getDaycareDistance().replace(",",""))) {
                return -1;
            }
            if (Double.parseDouble(d1.getDaycareDistance().replace(",","")) < Double.parseDouble(d2.getDaycareDistance().replace(",",""))) {
                return 1;
            }
            return 0;
        }
    };
}
