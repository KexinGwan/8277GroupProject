/********************************************************************************************************
 * File:  MedicalCertificate.java Course Materials CST 8277
 *
 * @author Teddy Yap
 * 
 */
package acmemedical.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import acmecollege.entity.MembershipCard;

@SuppressWarnings("unused")

/**
 * The persistent class for the medical_certificate database table.
 */
//TODO MC01 - Add the missing annotations.
//TODO MC02 - Do we need a mapped super class?  If so, which one?
@Entity
@Table(name = "medical_certificate")
@AttributeOverride(name = "id", column = @Column(name = "certificate_id"))
@NamedQuery(name = MedicalCertificate.ID_CERTIFICATE_QUERY_NAME, query =
"SELECT  ms FROM MedicalCertificate ms left join fetch ms.owner left join fetch ms.MedicalTraining WHERE mc.id=:param1")
@NamedQuery(name = MedicalCertificate.CERTIFICATE_QUERY_NAME, query =
"SELECT  ms FROM MedicalCertificate ms left join fetch ms.owner left join fetch ms.MedicalTraining ")
@NamedQuery(name = MedicalCertificate.CERTIFICATE_QUERY_By_Physician_MedicalTraining, query =
"SELECT  ms FROM MedicalCertificate ms left join fetch ms.owner left join fetch ms.MedicalTraining where ms.owner=:param1 and ms.MedicalTraining=:param2")
public class MedicalCertificate extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ID_CERTIFICATE_QUERY_NAME = "MedicalCertificate.findById";
	public static final String CERTIFICATE_QUERY_NAME = "MedicalCertificate.findAll";
	public static final String CERTIFICATE_QUERY_By_Physician_MedicalTraining ="MedicalCertificate.findphysicianandmedicaltraining";
	
	// TODO MC03 - Add annotations for 1:1 mapping.  What should be the cascade and fetch types?
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinColumn(name="training_id", referencedColumnName = "training_id")
	@JsonManagedReference(value = "medical_trainingCertificate")
	private MedicalTraining medicalTraining;

	// TODO MC04 - Add annotations for M:1 mapping.  What should be the cascade and fetch types?
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@JoinColumn(name = "physician_id", referencedColumnName = "id", nullable = false)
	 @JsonIgnoreProperties({"medicalCertificates"})
	private Physician owner;

	// TODO MC05 - Add annotations.
	@Basic(optional = false)
	@Column(name="signed", columnDefinition = "BIT(1)", nullable = false)
	private byte signed;

	public MedicalCertificate() {
		super();
	}
	
	public MedicalCertificate(MedicalTraining medicalTraining, Physician owner, byte signed) {
		this();
		this.medicalTraining = medicalTraining;
		this.owner = owner;
		this.signed = signed;
	}

	public MedicalTraining getMedicalTraining() {
		return medicalTraining;
	}

	public void setMedicalTraining(MedicalTraining medicalTraining) {
		this.medicalTraining = medicalTraining;
	}

	public Physician getOwner() {
		return owner;
	}

	public void setOwner(Physician owner) {
		this.owner = owner;
	}

	public byte getSigned() {
		return signed;
	}

	public void setSigned(byte signed) {
		this.signed = signed;
	}

	public void setSigned(boolean signed) {
		this.signed = (byte) (signed ? 0b0001 : 0b0000);
	}
	
	//Inherited hashCode/equals is sufficient for this entity class

}