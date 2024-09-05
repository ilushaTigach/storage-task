package org.telatenko.storagesevicedomain.seach.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;

public class LocalDateTimePredicateStrategy implements PredicateStrategy<LocalDateTime>{

    public Predicate getEqPattern(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder cb){
        return cb.equal(cb.function("date", LocalDateTime.class, expression), value.toLocalDate());
    }

    public Predicate getLeftLimitPattern(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder cb){
        return cb.greaterThanOrEqualTo(expression, value);
    }

    public Predicate getRightLimitPattern(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder cb){
        return cb.equal(expression, value);
    }

    public Predicate getLikePattern(Expression<LocalDateTime> expression, LocalDateTime value, CriteriaBuilder cb){
        return cb.between(expression, value.minusDays(3), value.plusDays(3));
    }
}
