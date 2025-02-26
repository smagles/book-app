package com.mate.bookstore.repository;

import org.springframework.data.jpa.domain.Specification;

/**
 * Provides specifications for filtering entities of type T.
 *
 * @param <T> the type of entity for which specifications are provided
 */
public interface SpecificationProvider<T> {
    /**
     * Returns the unique key identifying this specification provider.
     *
     * @return the provider's key
     */
    String getKey();

    /**
     * Creates a specification based on the given parameters.
     *
     * @param params the parameters to build the specification
     * @return the specification for filtering entities
     */
    Specification<T> getSpecification(String[] params);
}
