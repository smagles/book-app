package com.mate.bookstore.repository;

import com.mate.bookstore.exception.SpecificationNotFoundException;

/**
 * Manages specification providers for entity type T.
 *
 * @param <T> the type of entity for which specifications are provided
 */
public interface SpecificationProviderManager<T> {

    /**
     * Retrieves a specification provider for the given key.
     *
     * @param key the key identifying the specification provider
     * @return the specification provider for the given key
     * @throws SpecificationNotFoundException if no provider exists for the given key
     */
    SpecificationProvider<T> getSpecificationProvider(String key)
            throws SpecificationNotFoundException;
}
