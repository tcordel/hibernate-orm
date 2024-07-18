package org.hibernate.orm.test.query.convert;

import java.util.Date;

import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;

@DomainModel(
	annotatedClasses = {
		ConvertEntity.class,
	}
)
@SessionFactory
public class ConvertIssueTest {

	@Test
	public void testConvertError(SessionFactoryScope scope) throws Exception {
		final var creationDate = new Date();
		final var convertEntity = scope.fromTransaction(entityManager -> {
			final var convert = new ConvertEntity();
			convert.setId(1L);
			convert.setCreationDate(creationDate);
			entityManager.persist(convert);
			return convert;
		});
		scope.inTransaction(entityManager -> {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<ConvertEntity> criteriaQuery = criteriaBuilder.createQuery(ConvertEntity.class);
			Root<ConvertEntity> root = criteriaQuery.from(ConvertEntity.class);
			ParameterExpression<Date> createDateParameter = criteriaBuilder.parameter(Date.class);
			TypedQuery<ConvertEntity> query = entityManager
				.createQuery(criteriaQuery.where(criteriaBuilder.equal(root.get("creationDate"), createDateParameter)));
			query.setParameter(createDateParameter, creationDate);
			ConvertEntity entity = query.getSingleResult();
			Assertions.assertEquals(entity, convertEntity);
		});
	}
}
