package com.mate.bookstore.repository;

import org.springframework.data.jpa.domain.Specification;

/**
 * Builds specifications for filtering entities of type T.
 *
 * @param <T> the type of entity for which specifications are built
 * @param <P> the type of parameters used to build the specification
 */
public interface SpecificationBuilder<T, P> {
    /**
     * Builds a specification based on the given parameters.
     *
     * @param searchParameters the parameters to build the specification
     * @return the specification for filtering entities
     */
    Specification<T> build(P searchParameters);
}
