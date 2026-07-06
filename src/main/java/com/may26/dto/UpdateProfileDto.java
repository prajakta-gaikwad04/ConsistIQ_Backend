package com.may26.dto;

public class UpdateProfileDto {

    private String name;
    private String email;
    private String phone;
    private String city;
    private String country;
    private String bio;
    private String avatarStyle;
    private String profileImage;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    

public String getAvatarStyle() {
    return avatarStyle;
}

public void setAvatarStyle(String avatarStyle) {
    this.avatarStyle = avatarStyle;
}

public String getProfileImage() {
	return profileImage;
}

public void setProfileImage(String profileImage) {
	this.profileImage = profileImage;
}


}