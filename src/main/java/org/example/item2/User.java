package org.example.item2;

import java.io.Serializable;
import java.time.LocalDate;


public class User implements Serializable {
    // 필수값
    private final String name;
    private int age;
    private String id;
    private String password;

    public User(Builder builder) {
        this.name = builder.name;

    }


    public static class Builder {
        private final String name;
        private int age;
        private String id;
        private String password;
        private String address;

        private final LocalDate createdDatetime = LocalDate.now();

        public Builder(String name) {
            this.name = name;
        }

        public Builder age(int age) {
            this.age = age;
            return this;
        }



    }
}
