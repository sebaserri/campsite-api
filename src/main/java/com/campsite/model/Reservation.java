package com.campsite.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "reservations")
public class Reservation {

    @Id
    private ObjectId _id;

    private String bookingId;
    private String name;
    private String lastname;
    private String email;

    private long from;

    private long to;

    public String get_id() { return _id.toHexString(); }
    public void set_id(ObjectId _id) { this._id = _id; }

    public ObjectId getId() { return _id; }
}
