/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycompany.enums.BusinessType;
import com.mycompany.enums.VerificationStatus;
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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @author nguyen
 */
@Entity
@Table(name = "provider_profiles")
@NamedQueries({
    @NamedQuery(name = "ProviderProfiles.findAll", query = "SELECT p FROM ProviderProfiles p"),
    @NamedQuery(name = "ProviderProfiles.findById", query = "SELECT p FROM ProviderProfiles p WHERE p.id = :id"),
    @NamedQuery(name = "ProviderProfiles.findByCompanyName", query = "SELECT p FROM ProviderProfiles p WHERE p.companyName = :companyName"),
    @NamedQuery(name = "ProviderProfiles.findByBusinessType", query = "SELECT p FROM ProviderProfiles p WHERE p.businessType = :businessType"),
    @NamedQuery(name = "ProviderProfiles.findByVerificationStatus", query = "SELECT p FROM ProviderProfiles p WHERE p.verificationStatus = :verificationStatus"),
    @NamedQuery(name = "ProviderProfiles.findByCreatedAt", query = "SELECT p FROM ProviderProfiles p WHERE p.createdAt = :createdAt")})
public class ProviderProfiles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "company_name")
    private String companyName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "business_type")
    @Enumerated(EnumType.STRING)
    private BusinessType businessType;
    @Size(max = 8)
    @Column(name = "verification_status")
    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    @JsonIgnore
    private Users userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerId")
    @JsonIgnore
    private Set<Tours> toursSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerId")
    @JsonIgnore
    private Set<BusTrips> busTripsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerId")
    @JsonIgnore
    private Set<Reviews> reviewsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerId")
    @JsonIgnore
    private Set<Hotels> hotelsSet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "providerId")
    @JsonIgnore
    private Set<Flights> flightsSet;

    public ProviderProfiles() {
    }

    public ProviderProfiles(Long id) {
        this.id = id;
    }

    public ProviderProfiles(Long id, String companyName, BusinessType businessType) {
        this.id = id;
        this.companyName = companyName;
        this.businessType = businessType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public Set<Tours> getToursSet() {
        return toursSet;
    }

    public void setToursSet(Set<Tours> toursSet) {
        this.toursSet = toursSet;
    }

    public Set<BusTrips> getBusTripsSet() {
        return busTripsSet;
    }

    public void setBusTripsSet(Set<BusTrips> busTripsSet) {
        this.busTripsSet = busTripsSet;
    }

    public Set<Reviews> getReviewsSet() {
        return reviewsSet;
    }

    public void setReviewsSet(Set<Reviews> reviewsSet) {
        this.reviewsSet = reviewsSet;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProviderProfiles)) {
            return false;
        }
        ProviderProfiles other = (ProviderProfiles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.pojo.ProviderProfiles[ id=" + id + " ]";
    }

}
