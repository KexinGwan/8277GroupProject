/********************************************************************************************************
 * File:  MedicalSchool.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * 
 */
package acmemedical.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import acmecollege.entity.AcademicStudentClub;
import acmecollege.entity.NonAcademicStudentClub;
import acmecollege.entity.StudentClub;

/**
 * The persistent class for the medical_school database table.
 */
//TODO MS01 - Add the missing annotations.
//TODO MS02 - MedicalSchool has subclasses PublicSchool and PrivateSchool.  Look at Week 9 slides for InheritanceType.
//TODO MS03 - Do we need a mapped super class?  If so, which one?
//TODO MS04 - Add in JSON annotations to indicate different sub-classes of MedicalSchool
@Entity
@Table(name = "medical_school")
@AttributeOverride(name = "id", column = @Column(name = "school_id"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "public", columnDefinition = "BIT(1)", discriminatorType = DiscriminatorType.INTEGER)
@NamedQuery(name = MedicalSchool.SPECIFIC_MEDICAL_SCHOOL_QUERY_ID, query = "SELECT distinct ms FROM MedicalSchool ms left join fetch ms.medicalTrainings WHERE sc.id=:param1")
@NamedQuery(name = MedicalSchool.MEDICAL_SCHOOL_QUERY, query = "SELECT distinct ms FROM MedicalSchool ms left join fetch ms.medicalCertificates ")
@NamedQuery(name = MedicalSchool.IS_DUPLICATE_QUERY_NAME, query = "SELECT COUNT(ms.name) FROM MedicalSchool ms WHERE ms.name = :param1")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "entity-type")
@JsonSubTypes({ @Type(value = PublicSchool.class, name = "public_school"),
		@Type(value = PrivateSchool.class, name = "private_school") })

public abstract class MedicalSchool extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// TODO MS05 - Add the missing annotations.
	@Basic(optional = false)
	@Column(name = "name", nullable = false, length = 100)
	private String name;

	// TODO MS06 - Add the 1:M annotation.  What should be the cascade and fetch types?
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "school", orphanRemoval = true)
	@JsonIgnore
	private Set<MedicalTraining> medicalTrainings = new HashSet<>();

	// TODO MS07 - Add missing annotation.
	@Transient
	private boolean isPublic;

	public MedicalSchool() {
		super();
	}

    public MedicalSchool(boolean isPublic) {
        this();
        this.isPublic = isPublic;
    }

	// TODO MS08 - Is an annotation needed here?
    // No annotation needed here
	public Set<MedicalTraining> getMedicalTrainings() {
		return medicalTrainings;
	}

	public void setMedicalTrainings(Set<MedicalTraining> medicalTrainings) {
		this.medicalTrainings = medicalTrainings;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	//Inherited hashCode/equals is NOT sufficient for this entity class
	
	/**
	 * Very important:  Use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// Only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
		// they shouldn't be part of the hashCode calculation
		
		// The database schema for the MEDICAL_SCHOOL table has a UNIQUE constraint for the NAME column,
		// so we should include that in the hash/equals calculations
		
		return prime * result + Objects.hash(getId(), getName());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		
		if (obj instanceof MedicalSchool otherMedicalSchool) {
			// See comment (above) in hashCode():  Compare using only member variables that are
			// truly part of an object's identity
			return Objects.equals(this.getId(), otherMedicalSchool.getId()) &&
				Objects.equals(this.getName(), otherMedicalSchool.getName());
		}
		return false;
	}
}
