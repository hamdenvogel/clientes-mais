package io.github.hvogel.clientes.infra;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;



@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	protected int id;

	@CreatedBy
	protected String createdBy;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "tr_TR", timezone = "GMT+3")
	@CreatedDate
	protected Date createdDate;

	@LastModifiedBy
	protected String updatedBy;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "tr_TR", timezone = "GMT+3")
	@LastModifiedDate
	protected Date updatedDate;

	@Column
	protected boolean status = true;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, createdDate, id, status, updatedBy, updatedDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				&& id == other.id && status == other.status && Objects.equals(updatedBy, other.updatedBy)
				&& Objects.equals(updatedDate, other.updatedDate);
	}

	@Override
	public String toString() {
		return "BaseEntity [id=" + id + ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy="
				+ updatedBy + ", updatedDate=" + updatedDate + ", status=" + status + "]";
	}
	

}
