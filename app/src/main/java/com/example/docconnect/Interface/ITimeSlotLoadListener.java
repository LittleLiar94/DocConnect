package com.example.docconnect.Interface;

import com.example.docconnect.Model.Premise;
import com.example.docconnect.Model.TimeSlot;

import java.util.List;

public interface ITimeSlotLoadListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList, Premise premiseInfo);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty(List<TimeSlot> timeSlotList, Premise premiseInfo);
}
