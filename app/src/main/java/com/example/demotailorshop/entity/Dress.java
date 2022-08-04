package com.example.demotailorshop.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Dress implements Parcelable {
    private int dressId;
    private DressType dressType;
    private Customer customer;
    private String orderDate;
    private String orderTime;
    private String deliveryDate;
    private String deliveryTime;
    private String deliveryStatus;
    private int numberOfDress;
    private double price;
    private double discountedPrice;
    private String paymentStatus;
    private String comment;
    private Measurement measurement;

    public Dress() {
    }

    protected Dress(Parcel in) {
        dressId = in.readInt();
        orderDate = in.readString();
        orderTime = in.readString();
        deliveryDate = in.readString();
        deliveryTime = in.readString();
        deliveryStatus = in.readString();
        numberOfDress = in.readInt();
        price = in.readDouble();
        discountedPrice = in.readDouble();
        paymentStatus = in.readString();
        comment = in.readString();
    }

    public static final Creator<Dress> CREATOR = new Creator<Dress>() {
        @Override
        public Dress createFromParcel(Parcel in) {
            return new Dress(in);
        }

        @Override
        public Dress[] newArray(int size) {
            return new Dress[size];
        }
    };

    public int getDressId() {
        return dressId;
    }

    public void setDressId(int dressId) {
        this.dressId = dressId;
    }

    public DressType getDressType() {
        return dressType;
    }

    public void setDressType(DressType dressType) {
        this.dressType = dressType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public int getNumberOfDress() {
        return numberOfDress;
    }

    public void setNumberOfDress(int numberOfDress) {
        this.numberOfDress = numberOfDress;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    @NonNull
    @Override
    public String toString() {
        return "Dress{" +
                "dressId=" + dressId +
                ", dressType=" + dressType +
                ", customer=" + customer +
                ", orderDate='" + orderDate + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", numberOfDress=" + numberOfDress +
                ", price=" + price +
                ", discountedPrice=" + discountedPrice +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", comment='" + comment + '\'' +
                ", measurement=" + measurement +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dressId);
        dest.writeString(orderDate);
        dest.writeString(orderTime);
        dest.writeString(deliveryDate);
        dest.writeString(deliveryTime);
        dest.writeString(deliveryStatus);
        dest.writeInt(numberOfDress);
        dest.writeDouble(price);
        dest.writeDouble(discountedPrice);
        dest.writeString(paymentStatus);
        dest.writeString(comment);
    }
}


