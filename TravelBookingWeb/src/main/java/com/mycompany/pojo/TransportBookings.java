
package com.mycompany.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycompany.enums.TransportType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;


@Entity
@Table(name = "transport_bookings")
@NamedQueries({
    @NamedQuery(name = "TransportBookings.findAll", query = "SELECT t FROM TransportBookings t"),
    @NamedQuery(name = "TransportBookings.findById", query = "SELECT t FROM TransportBookings t WHERE t.id = :id"),
    @NamedQuery(name = "TransportBookings.findByTransportType", query = "SELECT t FROM TransportBookings t WHERE t.transportType = :transportType"),
    @NamedQuery(name = "TransportBookings.findByTransportServiceId", query = "SELECT t FROM TransportBookings t WHERE t.transportServiceId = :transportServiceId"),
    @NamedQuery(name = "TransportBookings.findBySeatNumber", query = "SELECT t FROM TransportBookings t WHERE t.seatNumber = :seatNumber")})
public class TransportBookings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "transport_type")
    @Enumerated(EnumType.STRING)
    private TransportType transportType;
    @Basic(optional = false)
    @NotNull
    @Column(name = "transport_service_id")
    private long transportServiceId;
    @Size(max = 20)
    @Column(name = "seat_number")
    private String seatNumber;
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    @JsonIgnore
    private Bookings bookingId;

    public TransportBookings() {
    }

    public TransportBookings(Long id) {
        this.id = id;
    }

    public TransportBookings(Long id, TransportType transportType, long transportServiceId) {
        this.id = id;
        this.transportType = transportType;
        this.transportServiceId = transportServiceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public long getTransportServiceId() {
        return transportServiceId;
    }

    public void setTransportServiceId(long transportServiceId) {
        this.transportServiceId = transportServiceId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Bookings getBookingId() {
        return bookingId;
    }

    public void setBookingId(Bookings bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof TransportBookings)) {
            return false;
        }
        TransportBookings other = (TransportBookings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.TransportBookings[ id=" + id + " ]";
    }

}
