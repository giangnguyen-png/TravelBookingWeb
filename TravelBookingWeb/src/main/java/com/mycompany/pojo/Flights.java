
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
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "flights")
@NamedQueries({
    @NamedQuery(name = "Flights.findAll", query = "SELECT f FROM Flights f"),
    @NamedQuery(name = "Flights.findById", query = "SELECT f FROM Flights f WHERE f.id = :id"),
    @NamedQuery(name = "Flights.findByFlightCode", query = "SELECT f FROM Flights f WHERE f.flightCode = :flightCode"),
    @NamedQuery(name = "Flights.findByDepartureTime", query = "SELECT f FROM Flights f WHERE f.departureTime = :departureTime"),
    @NamedQuery(name = "Flights.findByArrivalTime", query = "SELECT f FROM Flights f WHERE f.arrivalTime = :arrivalTime"),
    @NamedQuery(name = "Flights.findByPrice", query = "SELECT f FROM Flights f WHERE f.price = :price"),
    @NamedQuery(name = "Flights.findByAvailableSeats", query = "SELECT f FROM Flights f WHERE f.availableSeats = :availableSeats"),
    @NamedQuery(name = "Flights.findByThumbnail", query = "SELECT f FROM Flights f WHERE f.thumbnail = :thumbnail")})
public class Flights implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "flight_code")
    private String flightCode;
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

    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Column(name = "available_seats")
    private int availableSeats;
    @Size(max = 255)
    @Column(name = "thumbnail")
    private String thumbnail;
    @Transient
    private MultipartFile thumbnailFile;
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
    private ProviderProfiles providerId;

    public Flights() {
    }

    public Flights(Long id) {
        this.id = id;
    }

    public Flights(Long id, String flightCode, Date departureTime, Date arrivalTime, BigDecimal price, int availableSeats) {
        this.id = id;
        this.flightCode = flightCode;
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

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public MultipartFile getThumbnailFile() {
        return thumbnailFile;
    }

    public void setThumbnailFile(MultipartFile thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
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

        if (!(object instanceof Flights)) {
            return false;
        }
        Flights other = (Flights) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.Flights[ id=" + id + " ]";
    }

}
