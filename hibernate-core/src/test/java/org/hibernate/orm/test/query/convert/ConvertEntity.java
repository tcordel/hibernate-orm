package org.hibernate.orm.test.query.convert;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.Convert;

@Entity
@Table(name = "convert")
public class ConvertEntity {

	@Id
	private long id;

	@Convert(converter = DateConverter.class)
	private Date creationDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ConvertEntity entity = (ConvertEntity)o;
		return id == entity.id && Objects.equals(creationDate, entity.creationDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, creationDate);
	}
}
