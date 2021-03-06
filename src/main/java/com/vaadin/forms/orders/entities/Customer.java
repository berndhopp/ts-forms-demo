package com.vaadin.forms.orders.entities;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Customer extends IdEntity {

    @NotBlank
    @Size(max = 255)
    private String fullName;

    @NotBlank
    @Pattern(regexp = "^(\\+\\d+)?([ -]?\\d+){4,14}$", message = "Invalid phone number")
    private String phoneNumber;

    @Email(message = "Invalid email address")
    private String email;

    public Customer(Long id, String fullName, String email,
            String phoneNumber) {
        super(id);
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer [" + super.toString() + " + fullName=" + fullName
                + ", phoneNumber=" + phoneNumber + ", email=" + email + "]";
    }

}
