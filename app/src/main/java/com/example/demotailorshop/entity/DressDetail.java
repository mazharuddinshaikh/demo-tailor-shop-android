package com.example.demotailorshop.entity;

import java.util.List;

public class DressDetail {
    private Customer customer;
    private List<Dress> dressList;
    private Invoice customerInvoice;

    public DressDetail() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Dress> getDressList() {
        return dressList;
    }

    public void setDressList(List<Dress> dressList) {
        this.dressList = dressList;
    }

    public Invoice getCustomerInvoice() {
        return customerInvoice;
    }

    public void setCustomerInvoice(Invoice customerInvoice) {
        this.customerInvoice = customerInvoice;
    }
}
