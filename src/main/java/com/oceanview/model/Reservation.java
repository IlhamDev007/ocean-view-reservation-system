package com.oceanview.model;

import java.sql.Date;

public class Reservation {
    private int reservationId;
    private String guestName;
    private String guestAddress;
    private String contactNumber;
    private String roomType;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation() {}

    public Reservation(String guestName, String guestAddress, String contactNumber,
                       String roomType, Date checkInDate, Date checkOutDate) {
        this.guestName = guestName;
        this.guestAddress = guestAddress;
        this.contactNumber = contactNumber;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public String getGuestAddress() { return guestAddress; }
    public void setGuestAddress(String guestAddress) { this.guestAddress = guestAddress; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public Date getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Date checkInDate) { this.checkInDate = checkInDate; }

    public Date getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Date checkOutDate) { this.checkOutDate = checkOutDate; }
}