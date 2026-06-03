
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
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;


@Entity
@Table(name = "tour_bookings")
@NamedQueries({
    @NamedQuery(name = "TourBookings.findAll", query = "SELECT t FROM TourBookings t"),
    @NamedQuery(name = "TourBookings.findById", query = "SELECT t FROM TourBookings t WHERE t.id = :id"),
    @NamedQuery(name = "TourBookings.findByNumberOfPeople", query = "SELECT t FROM TourBookings t WHERE t.numberOfPeople = :numberOfPeople")})
public class TourBookings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "number_of_people")
    private int numberOfPeople;
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    @JsonIgnore
    private Bookings bookingId;
    @JoinColumn(name = "tour_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Tours tourId;

    public TourBookings() {
    }

    public TourBookings(Long id) {
        this.id = id;
    }

    public TourBookings(Long id, int numberOfPeople) {
        this.id = id;
        this.numberOfPeople = numberOfPeople;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public Bookings getBookingId() {
        return bookingId;
    }

    public void setBookingId(Bookings bookingId) {
        this.bookingId = bookingId;
    }

    public Tours getTourId() {
        return tourId;
    }

    public void setTourId(Tours tourId) {
        this.tourId = tourId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TourBookings)) {
            return false;
        }
        TourBookings other = (TourBookings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.TourBookings[ id=" + id + " ]";
    }

}
