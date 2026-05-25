/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nguyen
 */
@Entity
@Table(name = "tours")
@NamedQueries({
    @NamedQuery(name = "Tours.findAll", query = "SELECT t FROM Tours t"),
    @NamedQuery(name = "Tours.findById", query = "SELECT t FROM Tours t WHERE t.id = :id"),
    @NamedQuery(name = "Tours.findByTitle", query = "SELECT t FROM Tours t WHERE t.title = :title"),
    @NamedQuery(name = "Tours.findByDepartureDate", query = "SELECT t FROM Tours t WHERE t.departureDate = :departureDate"),
    @NamedQuery(name = "Tours.findByDurationDays", query = "SELECT t FROM Tours t WHERE t.durationDays = :durationDays"),
    @NamedQuery(name = "Tours.findByPrice", query = "SELECT t FROM Tours t WHERE t.price = :price"),
    @NamedQuery(name = "Tours.findByAvailableSlots", query = "SELECT t FROM Tours t WHERE t.availableSlots = :availableSlots"),
    @NamedQuery(name = "Tours.findByThumbnail", query = "SELECT t FROM Tours t WHERE t.thumbnail = :thumbnail"),
    @NamedQuery(name = "Tours.findByCreatedAt", query = "SELECT t FROM Tours t WHERE t.createdAt = :createdAt")})
public class Tours implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "departure_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date departureDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "duration_days")
    private int durationDays;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private BigDecimal price;
    @Basic(optional = false)
    @NotNull
    @Column(name = "available_slots")
    private int availableSlots;
    @Size(max = 255)
    @Column(name = "thumbnail")
    private String thumbnail;
    @Transient
    private MultipartFile thumbnailFile;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tourId")
    @JsonIgnore
    private Set<TourBookings> tourBookingsSet;
    @JoinColumn(name = "departure_location_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Locations departureLocationId;
    @JoinColumn(name = "destination_location_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Locations destinationLocationId;
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private ProviderProfiles providerId;

    public Tours() {
    }

    public Tours(Long id) {
        this.id = id;
    }

    public Tours(Long id, String title, Date departureDate, int durationDays, BigDecimal price, int availableSlots) {
        this.id = id;
        this.title = title;
        this.departureDate = departureDate;
        this.durationDays = durationDays;
        this.price = price;
        this.availableSlots = availableSlots;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Set<TourBookings> getTourBookingsSet() {
        return tourBookingsSet;
    }

    public void setTourBookingsSet(Set<TourBookings> tourBookingsSet) {
        this.tourBookingsSet = tourBookingsSet;
    }

    public Locations getDepartureLocationId() {
        return departureLocationId;
    }

    public void setDepartureLocationId(Locations departureLocationId) {
        this.departureLocationId = departureLocationId;
    }

    public Locations getDestinationLocationId() {
        return destinationLocationId;
    }

    public void setDestinationLocationId(Locations destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
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
        if (!(object instanceof Tours)) {
            return false;
        }
        Tours other = (Tours) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.Tours[ id=" + id + " ]";
    }
    
}
