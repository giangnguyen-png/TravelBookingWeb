
package com.mycompany.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;


@Entity
@Table(name = "locations")
@NamedQueries({
    @NamedQuery(name = "Locations.findAll", query = "SELECT l FROM Locations l"),
    @NamedQuery(name = "Locations.findById", query = "SELECT l FROM Locations l WHERE l.id = :id"),
    @NamedQuery(name = "Locations.findByProvince", query = "SELECT l FROM Locations l WHERE l.province = :province"),
    @NamedQuery(name = "Locations.findByCountry", query = "SELECT l FROM Locations l WHERE l.country = :country")})
public class Locations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "province")
    private String province;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "country")
    private String country;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departureLocationId")
    @JsonIgnore
    private Set<Tours> toursSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "destinationLocationId")
    @JsonIgnore
    private Set<Tours> toursSet1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departureLocationId")
    @JsonIgnore
    private Set<BusTrips> busTripsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "arrivalLocationId")
    @JsonIgnore
    private Set<BusTrips> busTripsSet1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "locationId")
    @JsonIgnore
    private Set<Hotels> hotelsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departureLocationId")
    @JsonIgnore
    private Set<Flights> flightsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "arrivalLocationId")
    @JsonIgnore
    private Set<Flights> flightsSet1;

    public Locations() {
    }

    public Locations(Long id) {
        this.id = id;
    }

    public Locations(Long id, String province, String country) {
        this.id = id;
        this.province = province;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Tours> getToursSet() {
        return toursSet;
    }

    public void setToursSet(Set<Tours> toursSet) {
        this.toursSet = toursSet;
    }

    public Set<Tours> getToursSet1() {
        return toursSet1;
    }

    public void setToursSet1(Set<Tours> toursSet1) {
        this.toursSet1 = toursSet1;
    }

    public Set<BusTrips> getBusTripsSet() {
        return busTripsSet;
    }

    public void setBusTripsSet(Set<BusTrips> busTripsSet) {
        this.busTripsSet = busTripsSet;
    }

    public Set<BusTrips> getBusTripsSet1() {
        return busTripsSet1;
    }

    public void setBusTripsSet1(Set<BusTrips> busTripsSet1) {
        this.busTripsSet1 = busTripsSet1;
    }

    public Set<Hotels> getHotelsSet() {
        return hotelsSet;
    }

    public void setHotelsSet(Set<Hotels> hotelsSet) {
        this.hotelsSet = hotelsSet;
    }

    public Set<Flights> getFlightsSet() {
        return flightsSet;
    }

    public void setFlightsSet(Set<Flights> flightsSet) {
        this.flightsSet = flightsSet;
    }

    public Set<Flights> getFlightsSet1() {
        return flightsSet1;
    }

    public void setFlightsSet1(Set<Flights> flightsSet1) {
        this.flightsSet1 = flightsSet1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Locations)) {
            return false;
        }
        Locations other = (Locations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.Locations[ id=" + id + " ]";
    }

}
