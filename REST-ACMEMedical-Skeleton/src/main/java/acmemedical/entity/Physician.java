/********************************************************************************************************
 * File:  Physician.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * 
 */
package acmemedical.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the physician database table.
 */
@SuppressWarnings("unused")

//TODO PH01 - Add the missing annotations.
//TODO PH02 - Do we need a mapped super class? If so, which one?
@Entity
@Table(name = "Physician")
public class Physician extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

    public Physician() {
    	super();
    }

	// TODO PH03 - Add annotations.
    @Basic(optional = false)
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	// TODO PH04 - Add annotations.
    @Basic(optional = false)
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	// TODO PH05 - Add annotations for 1:M relation.  What should be the cascade and fetch types?
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "owner")
	@JsonBackReference
	private Set<MedicalCertificate> medicalCertificates = new HashSet<>();

	// TODO PH06 - Add annotations for 1:M relation.  What should be the cascade and fetch types?
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "physician")
	@JsonIgnore
	private Set<Prescription> prescriptions = new HashSet<>();

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	// TODO PH07 - Is an annotation needed here?
	//no need
    public Set<MedicalCertificate> getMedicalCertificates() {
		return medicalCertificates;
	}

	public void setMedicalCertificates(Set<MedicalCertificate> medicalCertificates) {
		this.medicalCertificates = medicalCertificates;
	}

	// TODO PH08 - Is an annotation needed here?
	@JsonIgnore
    public Set<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public void setPrescriptions(Set<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}

	public void setFullName(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}
	
	//Inherited hashCode/equals is sufficient for this entity class

}
