package com.example.groceryshoppingsystem.Model;

public class AdminOffer {
    private String OfferName , OfferDescription;
    private String OfferImg;

    public AdminOffer(String offerName, String offerDescription, String offerImg) {
        OfferName = offerName;
        OfferDescription = offerDescription;
        OfferImg = offerImg;
    }

    public String getOfferName() {
        return OfferName;
    }

    public void setOfferName(String offerName) {
        OfferName = offerName;
    }

    public String getOfferDescription() {
        return OfferDescription;
    }

    public void setOfferDescription(String offerDescription) {
        OfferDescription = offerDescription;
    }

    public String getOfferImg() {
        return OfferImg;
    }

    public void setOfferImg(String offerImg) {
        OfferImg = offerImg;
    }
}
