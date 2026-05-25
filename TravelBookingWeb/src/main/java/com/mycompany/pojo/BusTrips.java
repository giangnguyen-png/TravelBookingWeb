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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author nguyen
 */
@Entity
@Table(name = "bus_trips")
@NamedQueries({
    @NamedQuery(name = "BusTrips.findAll", query = "SELECT b FROM BusTrips b"),
    @NamedQuery(name = "BusTrips.findById", query = "SELECT b FROM BusTrips b WHERE b.id = :id"),
    @NamedQuery(name = "BusTrips.findByDepartureTime", query = "SELECT b FROM BusTrips b WHERE b.departureTime = :departureTime"),
    @NamedQuery(name = "BusTrips.findByArrivalTime", query = "SELECT b FROM BusTrips b WHERE b.arrivalTime = :arrivalTime"),
    @NamedQuery(name = "BusTrips.findByPrice", query = "SELECT b FROM BusTrips b WHERE b.price = :price"),
    @NamedQuery(name = "BusTrips.findByAvailableSeats", query = "SELECT b FROM BusTrips b WHERE b.availableSeats = :availableSeats")})
public class BusTrips implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "departure_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "arrival_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrivalTime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Column(name = "available_seats")
    private int availableSeats;
    @JoinColumn(name = "departure_location_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Locations departureLocationId;
    @JoinColumn(name = "arrival_location_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Locations arrivalLocationId;
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private ProviderProfiles providerId;

    public BusTrips() {
    }

    public BusTrips(Long id) {
        this.id = id;
    }

    public BusTrips(Long id, Date departureTime, Date arrivalTime, BigDecimal price, int availableSeats) {
        this.id = id;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Locations getDepartureLocationId() {
        return departureLocationId;
    }

    public void setDepartureLocationId(Locations departureLocationId) {
        this.departureLocationId = departureLocationId;
    }

    public Locations getArrivalLocationId() {
        return arrivalLocationId;
    }

    public void setArrivalLocationId(Locations arrivalLocationId) {
        this.arrivalLocationId = arrivalLocationId;
    }

    public ProviderProfiles getProviderId() {
        return providerId;
    }

    public void setProviderId(ProviderProfiles providerId) {
        this.providerId = providerId;
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
        if (!(object instanceof BusTrips)) {
            return false;
        }
        BusTrips other = (BusTrips) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.BusTrips[ id=" + id + " ]";
    }
    
}
