package com.example.client.service;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


public class ConsumerDTO implements Serializable {

    private Long id;


    private String firstName;

    private String lastName;

    private String idnp;

    private LocalDate registrationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIdnp() {
        return idnp;
    }

    public void setIdnp(String idnp) {
        this.idnp = idnp;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumerDTO)) {
            return false;
        }

        ConsumerDTO consumerDTO = (ConsumerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consumerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumerDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", idnp='" + getIdnp() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            "}";
    }
}
