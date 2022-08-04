package com.example.demotailorshop.listener;

import com.example.demotailorshop.entity.Dress;
import com.example.demotailorshop.entity.DressDetail;

public interface DressDetailListener {
    void onUpdateDressDetail(DressDetail dressDetail);
    void onUpdateDress(Dress dress);
}
