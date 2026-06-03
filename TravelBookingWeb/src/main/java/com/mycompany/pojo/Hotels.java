
package com.mycompany.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import java.util.Date;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "hotels")
@NamedQueries({
    @NamedQuery(name = "Hotels.findAll", query = "SELECT h FROM Hotels h"),
    @NamedQuery(name = "Hotels.findById", query = "SELECT h FROM Hotels h WHERE h.id = :id"),
    @NamedQuery(name = "Hotels.findByHotelName", query = "SELECT h FROM Hotels h WHERE h.hotelName = :hotelName"),
    @NamedQuery(name = "Hotels.findByAddress", query = "SELECT h FROM Hotels h WHERE h.address = :address"),
    @NamedQuery(name = "Hotels.findByThumbnail", query = "SELECT h FROM Hotels h WHERE h.thumbnail = :thumbnail"),
    @NamedQuery(name = "Hotels.findByCreatedAt", query = "SELECT h FROM Hotels h WHERE h.createdAt = :createdAt")})
public class Hotels implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "hotel_name")
    private String hotelName;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    @Size(max = 255)
    @Column(name = "thumbnail")
    private String thumbnail;
    @Transient
    private MultipartFile thumbnailFile;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Locations locationId;
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProviderProfiles providerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotelId", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<HotelRooms> hotelRoomsSet;

    public Hotels() {
    }

    public Hotels(Long id) {
        this.id = id;
    }

    public Hotels(Long id, String hotelName) {
        this.id = id;
        this.hotelName = hotelName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Locations getLocationId() {
        return locationId;
    }

    public void setLocationId(Locations locationId) {
        this.locationId = locationId;
    }

    public ProviderProfiles getProviderId() {
        return providerId;
    }

    public void setProviderId(ProviderProfiles providerId) {
        this.providerId = providerId;
    }

    public Set<HotelRooms> getHotelRoomsSet() {
        return hotelRoomsSet;
    }

    public void setHotelRoomsSet(Set<HotelRooms> hotelRoomsSet) {
        this.hotelRoomsSet = hotelRoomsSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Hotels)) {
            return false;
        }
        Hotels other = (Hotels) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.Hotels[ id=" + id + " ]";
    }
    public Long getPrice() {
        if (this.hotelRoomsSet != null && !this.hotelRoomsSet.isEmpty()) {


            return this.hotelRoomsSet.iterator().next().getPricePerNight().longValue();
        }
    return 0L;
    }

}
