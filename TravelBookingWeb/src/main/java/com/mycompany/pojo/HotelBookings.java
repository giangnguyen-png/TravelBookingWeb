/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author nguyen
 */
@Entity
@Table(name = "hotel_bookings")
@NamedQueries({
    @NamedQuery(name = "HotelBookings.findAll", query = "SELECT h FROM HotelBookings h"),
    @NamedQuery(name = "HotelBookings.findById", query = "SELECT h FROM HotelBookings h WHERE h.id = :id"),
    @NamedQuery(name = "HotelBookings.findByCheckInDate", query = "SELECT h FROM HotelBookings h WHERE h.checkInDate = :checkInDate"),
    @NamedQuery(name = "HotelBookings.findByCheckOutDate", query = "SELECT h FROM HotelBookings h WHERE h.checkOutDate = :checkOutDate"),
    @NamedQuery(name = "HotelBookings.findByNumberOfRooms", query = "SELECT h FROM HotelBookings h WHERE h.numberOfRooms = :numberOfRooms")})
public class HotelBookings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "check_in_date")
    @Temporal(TemporalType.DATE)
    private Date checkInDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "check_out_date")
    @Temporal(TemporalType.DATE)
    private Date checkOutDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "number_of_rooms")
    private int numberOfRooms;
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    @JsonIgnore
    private Bookings bookingId;
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private HotelRooms roomId;

    public HotelBookings() {
    }

    public HotelBookings(Long id) {
        this.id = id;
    }

    public HotelBookings(Long id, Date checkInDate, Date checkOutDate, int numberOfRooms) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfRooms = numberOfRooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Bookings getBookingId() {
        return bookingId;
    }

    public void setBookingId(Bookings bookingId) {
        this.bookingId = bookingId;
    }

    public HotelRooms getRoomId() {
        return roomId;
    }

    public void setRoomId(HotelRooms roomId) {
        this.roomId = roomId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HotelBookings)) {
            return false;
        }
        HotelBookings other = (HotelBookings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.HotelBookings[ id=" + id + " ]";
    }
    
}
