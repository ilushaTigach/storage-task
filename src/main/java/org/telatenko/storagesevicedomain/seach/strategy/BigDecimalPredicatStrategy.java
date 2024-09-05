package org.telatenko.storagesevicedomain.seach.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import java.math.BigDecimal;

public class BigDecimalPredicatStrategy implements PredicateStrategy<BigDecimal> {

    public Predicate getEqPattern(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder cb){
        return cb.equal(expression, value);
    }

    public Predicate getLeftLimitPattern(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder cb){
        return cb.greaterThanOrEqualTo(expression, value);
    }

    public Predicate getRightLimitPattern(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder cb){
        return cb.equal(expression, value);
    }

    public Predicate getLikePattern(Expression<BigDecimal> expression, BigDecimal value, CriteriaBuilder cb){
        return cb.and(
                cb.greaterThanOrEqualTo(expression, value.multiply(BigDecimal.valueOf(0.9))),
                cb.lessThanOrEqualTo(expression, value.multiply(BigDecimal.valueOf(1.1)))
        );
    }
}

