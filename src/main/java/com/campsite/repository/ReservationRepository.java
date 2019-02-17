package com.campsite.repository;

import com.campsite.model.Reservation;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository <Reservation, String> {

    Reservation findByBookingId(String bookingId);

    //long countByDateFromGreaterThanEqualAndDateToLessThan(Date from, Date to);

    List<Reservation> findByFromGreaterThanEqual(long dateFrom);


    long countByFromGreaterThanEqual(long dateFrom);

    long countAllByFromGreaterThanEqualAndToLessThanEqual(long from, long to);

    // List<Reservation> findAllByDateFromGreaterThanEqualAndDateToLessThan(Long from, Long to);
}
