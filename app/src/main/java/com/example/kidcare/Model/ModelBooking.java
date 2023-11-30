package com.example.kidcare.Model;

import java.util.Comparator;

public class ModelBooking {
    String daycareName;
    String childName;
    String date;
    String status;
    String daycareID;
    String bookingID;
    String price;
    String accountNumber;

    public String getPrice() {
        return price;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public ModelBooking(String daycareName, String childName, String date, String status, String daycareID, String bookingID, String price, String accountNumber) {
        this.daycareName = daycareName;
        this.childName = childName;
        this.date = date;
        this.status = status;
        this.daycareID = daycareID;
        this.bookingID =  bookingID;
        this.price = price;
        this.accountNumber = accountNumber;
    }

    public String getDaycareName() {
        return daycareName;
    }

    public String getChildName() {
        return childName;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getDaycareID() { return daycareID; }
    public String getBookingID() { return bookingID; }

    public static Comparator<ModelBooking> bookingZAComparator = new Comparator<ModelBooking>() {
        @Override
        public int compare(ModelBooking d1, ModelBooking d2) {
            if (Double.parseDouble(d1.getDate().replace("-", "")) < Double.parseDouble(d2.getDate().replace("-", ""))) {
                return 1;
            }
            if (Double.parseDouble(d1.getDate().replace("-", "")) > Double.parseDouble(d2.getDate().replace("-", ""))) {
                return -1;
            }
            return 0;
//            return d2.getDaycarePrice().replace(".","").compareTo(d1.getDaycarePrice().replace(".",""));
        }
    };
}
