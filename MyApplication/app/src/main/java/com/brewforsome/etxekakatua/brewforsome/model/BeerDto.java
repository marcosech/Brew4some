package com.brewforsome.etxekakatua.brewforsome.model;

/**
 * Created by etxekakatua on 1/12/17.
 */

public class BeerDto {

    public String name;
    public String style;
    public String comments;
    public String malt;
    public String hop;
    public String yeast;
    public String extra;
    public String interests;
    public String location;
    public String userEmail;
    public String phone;
    public String abv;
    public String brewerName;

    public BeerDto() {
    }

    public BeerDto(String name, String style, String comments, String malt, String hop, String yeast, String extra, String interests, String location, String userEmail, String phone, String abv, String brewerName) {
        this.name = name;
        this.style = style;
        this.comments = comments;
        this.malt = malt;
        this.hop = hop;
        this.yeast = yeast;
        this.extra = extra;
        this.interests = interests;
        this.location = location;
        this.userEmail = userEmail;
        this.phone = phone;
        this.abv = abv;
        this.brewerName = brewerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getMalt() {
        return malt;
    }

    public void setMalt(String malt) {
        this.malt = malt;
    }

    public String getHop() {
        return hop;
    }

    public void setHop(String hop) {
        this.hop = hop;
    }

    public String getYeast() {
        return yeast;
    }

    public void setYeast(String yeast) {
        this.yeast = yeast;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getBrewerName() {
        return brewerName;
    }

    public void setBrewerName(String brewerName) {
        this.brewerName = brewerName;
    }
}