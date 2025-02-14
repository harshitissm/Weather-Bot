package com.bot.cloudy.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Subscription {

    @Id
    private ObjectId id;

    @Field("userId")
    private Long userId;

    @Field("state")
    private String state;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public Subscription(Long userId, String state) {
        this.userId = userId;
        this.state = state;
    }

}
