
package com.mycompany.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycompany.enums.BookingStatus;
import com.mycompany.enums.BookingType;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(name = "bookings")
@NamedQueries({
    @NamedQuery(name = "Bookings.findAll", query = "SELECT b FROM Bookings b"),
    @NamedQuery(name = "Bookings.findById", query = "SELECT b FROM Bookings b WHERE b.id = :id"),
    @NamedQuery(name = "Bookings.findByBookingType", query = "SELECT b FROM Bookings b WHERE b.bookingType = :bookingType"),
    @NamedQuery(name = "Bookings.findByTotalPrice", query = "SELECT b FROM Bookings b WHERE b.totalPrice = :totalPrice"),
    @NamedQuery(name = "Bookings.findByStatus", query = "SELECT b FROM Bookings b WHERE b.status = :status"),
    @NamedQuery(name = "Bookings.findByCreatedAt", query = "SELECT b FROM Bookings b WHERE b.createdAt = :createdAt")})
public class Bookings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_type")
    private BookingType bookingType;

    @Basic(optional = false)
    @NotNull
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "bookingId")
    @JsonIgnore
    private TransportBookings transportBookings;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "bookingId")
    @JsonIgnore
    private Payments payments;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "bookingId")
    @JsonIgnore
    private TourBookings tourBookings;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "bookingId")
    @JsonIgnore
    private HotelBookings hotelBookings;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users customerId;

    public Bookings() {
    }

    public Bookings(Long id) {
        this.id = id;
    }

    public Bookings(Long id, BookingType bookingType, BigDecimal totalPrice) {
        this.id = id;
        this.bookingType = bookingType;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingType getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingType bookingType) {
        this.bookingType = bookingType;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public TransportBookings getTransportBookings() {
        return transportBookings;
    }

    public void setTransportBookings(TransportBookings transportBookings) {
        this.transportBookings = transportBookings;
    }

    public Payments getPayments() {
        return payments;
    }

    public void setPayments(Payments payments) {
        this.payments = payments;
    }

    public TourBookings getTourBookings() {
        return tourBookings;
    }

    public void setTourBookings(TourBookings tourBookings) {
        this.tourBookings = tourBookings;
    }

    public HotelBookings getHotelBookings() {
        return hotelBookings;
    }

    public void setHotelBookings(HotelBookings hotelBookings) {
        this.hotelBookings = hotelBookings;
    }

    public Users getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Users customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Bookings)) {
            return false;
        }
        Bookings other = (Bookings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.Bookings[ id=" + id + " ]";
    }

}
