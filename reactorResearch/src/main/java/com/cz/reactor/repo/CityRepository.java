package com.cz.reactor.repo;

import com.cz.reactor.domai.City;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * code desc
 *
 * @author Zjianru
 */
@Repository
public interface CityRepository extends ReactiveMongoRepository<City, Long> {

}
