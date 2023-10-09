package com.example.evcharging;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.UUID;
public class Booking implements Parcelable {
    private String userId;
    private String chargingStationId;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private int paymentAmount; // Payment amount field
    private String paymentStatus; // Payment status field
    private String paymentMethod;


        private String bookingId;


    // Constructor and other methods...

    // Calculate the duration in hours
    public int getDurationInHours() {
        // Get the current date and time
        Calendar currentTime = Calendar.getInstance();
        int currentYear = currentTime.get(Calendar.YEAR);
        int currentMonth = currentTime.get(Calendar.MONTH) + 1; // Months are zero-based
        int currentDay = currentTime.get(Calendar.DAY_OF_MONTH);
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentTime.get(Calendar.MINUTE);

        // Calculate the difference in years, months, days, hours, and minutes
        int yearDiff = year - currentYear;
        int monthDiff = month - currentMonth;
        int dayDiff = day - currentDay;
        int hourDiff = hour - currentHour;
        int minuteDiff = minute - currentMinute;

        // Calculate the total duration in hours
        int durationInHours = (yearDiff * 365 * 24) + (monthDiff * 30 * 24) + (dayDiff * 24) + hourDiff + (minuteDiff / 60);

        return durationInHours;
    }// The booking ID field

        // Constructor and other methods

        // Generate a random booking ID and set it during object creation
        public Booking() {
            this.bookingId = generateRandomBookingId();
        }

        // Generate a random booking ID
        private String generateRandomBookingId() {
            // Use UUID to generate a random booking ID
            return UUID.randomUUID().toString();
        }

        // Get the booking ID
        public String getBookingId() {
            return bookingId;
        }

        // Other getters and setters
// Payment method field



    public Booking(String userId, String chargingStationId, int day, int month, int year, int hour, int minute) {
        this.userId = userId;
        this.chargingStationId = chargingStationId;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    public Booking(Parcel in) {
        userId = in.readString();
        chargingStationId = in.readString();
        day = in.readInt();
        month = in.readInt();
        year = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        paymentAmount = in.readInt();
        paymentStatus = in.readString();
        paymentMethod = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(chargingStationId);
        dest.writeInt(day);
        dest.writeInt(month);
        dest.writeInt(year);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(paymentAmount);
        dest.writeString(paymentStatus);
        dest.writeString(paymentMethod);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public String getChargingStationId() {
        return chargingStationId;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


}
