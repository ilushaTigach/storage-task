package org.telatenko.storagesevicedomain.seach.strategy;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class StringPredicateStrategy implements PredicateStrategy<String> {

    public Predicate getEqPattern(Expression<String> expression, String value, CriteriaBuilder cb){
        return cb.equal(expression, value);
    }

    public Predicate getLeftLimitPattern(Expression<String> expression, String value, CriteriaBuilder cb){
        return cb.like(expression, value + "%");
    }

    public Predicate getRightLimitPattern(Expression<String> expression, String value, CriteriaBuilder cb){
        return cb.like(expression, "%" + value);
    }

    public Predicate getLikePattern(Expression<String> expression, String value, CriteriaBuilder cb){
        return cb.like(cb.lower(expression), "%" + value.toLowerCase() + "%");
    }
}
