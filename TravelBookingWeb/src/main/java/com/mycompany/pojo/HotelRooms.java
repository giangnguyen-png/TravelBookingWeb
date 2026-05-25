/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycompany.enums.RoomType;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nguyen
 */
@Entity
@Table(name = "hotel_rooms")
@NamedQueries({
    @NamedQuery(name = "HotelRooms.findAll", query = "SELECT h FROM HotelRooms h"),
    @NamedQuery(name = "HotelRooms.findById", query = "SELECT h FROM HotelRooms h WHERE h.id = :id"),
    @NamedQuery(name = "HotelRooms.findByRoomName", query = "SELECT h FROM HotelRooms h WHERE h.roomName = :roomName"),
    @NamedQuery(name = "HotelRooms.findByRoomType", query = "SELECT h FROM HotelRooms h WHERE h.roomType = :roomType"),
    @NamedQuery(name = "HotelRooms.findByPricePerNight", query = "SELECT h FROM HotelRooms h WHERE h.pricePerNight = :pricePerNight"),
    @NamedQuery(name = "HotelRooms.findByAvailableRooms", query = "SELECT h FROM HotelRooms h WHERE h.availableRooms = :availableRooms"),
    @NamedQuery(name = "HotelRooms.findByImage", query = "SELECT h FROM HotelRooms h WHERE h.image = :image")})
public class HotelRooms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "room_name")
    private String roomName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "room_type")
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "price_per_night")
    private BigDecimal pricePerNight;
    @Basic(optional = false)
    @NotNull
    @Column(name = "available_rooms")
    private int availableRooms;
    @Lob
    @Size(max = 65535)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "image")
    private String image;
    @Transient
    private MultipartFile imageFile;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomId")
    @JsonIgnore
    private Set<HotelBookings> hotelBookingsSet;
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Hotels hotelId;

    public HotelRooms() {
    }

    public HotelRooms(Long id) {
        this.id = id;
    }

    public HotelRooms(Long id, String roomName, RoomType roomType, BigDecimal pricePerNight, int availableRooms) {
        this.id = id;
        this.roomName = roomName;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.availableRooms = availableRooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        this.availableRooms = availableRooms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public Set<HotelBookings> getHotelBookingsSet() {
        return hotelBookingsSet;
    }

    public void setHotelBookingsSet(Set<HotelBookings> hotelBookingsSet) {
        this.hotelBookingsSet = hotelBookingsSet;
    }

    public Hotels getHotelId() {
        return hotelId;
    }

    public void setHotelId(Hotels hotelId) {
        this.hotelId = hotelId;
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
        if (!(object instanceof HotelRooms)) {
            return false;
        }
        HotelRooms other = (HotelRooms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.HotelRooms[ id=" + id + " ]";
    }
    
}
