package com.madhavsteel.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sizes implements Parcelable {

    String position, cartId, sizeId, productId, productTitle, catId, catTitle, sizeTitle, weight, priceType, nosKg, sizeImage,
            sizeThumbImage, sizeMediumImage, height, width, round, square, rectangle, hexagon, octagon, quantity, measurement, price,
            pieceQuantity, cartStatus, showPieceStatus, status, dateTimeAdded;

    public Sizes() {
    }

    protected Sizes(Parcel in) {
        position = in.readString();
        cartId = in.readString();
        sizeId = in.readString();
        productId = in.readString();
        productTitle = in.readString();
        catId = in.readString();
        catTitle = in.readString();
        sizeTitle = in.readString();
        weight = in.readString();
        priceType = in.readString();
        nosKg = in.readString();
        price = in.readString();
        sizeImage = in.readString();
        sizeThumbImage = in.readString();
        sizeMediumImage = in.readString();
        height = in.readString();
        width = in.readString();
        round = in.readString();
        square = in.readString();
        rectangle = in.readString();
        hexagon = in.readString();
        octagon = in.readString();
        quantity = in.readString();
        measurement = in.readString();
        pieceQuantity = in.readString();
        cartStatus = in.readString();
        showPieceStatus = in.readString();
        status = in.readString();
        dateTimeAdded = in.readString();
    }

    public static final Creator<Sizes> CREATOR = new Creator<Sizes>() {
        @Override
        public Sizes createFromParcel(Parcel in) {
            return new Sizes(in);
        }

        @Override
        public Sizes[] newArray(int size) {
            return new Sizes[size];
        }
    };

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCatTitle() {
        return catTitle;
    }

    public void setCatTitle(String catTitle) {
        this.catTitle = catTitle;
    }

    public String getSizeTitle() {
        return sizeTitle;
    }

    public void setSizeTitle(String sizeTitle) {
        this.sizeTitle = sizeTitle;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getNosKg() {
        return nosKg;
    }

    public void setNosKg(String nosKg) {
        this.nosKg = nosKg;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSizeImage() {
        return sizeImage;
    }

    public void setSizeImage(String sizeImage) {
        this.sizeImage = sizeImage;
    }

    public String getSizeThumbImage() {
        return sizeThumbImage;
    }

    public void setSizeThumbImage(String sizeThumbImage) {
        this.sizeThumbImage = sizeThumbImage;
    }

    public String getSizeMediumImage() {
        return sizeMediumImage;
    }

    public void setSizeMediumImage(String sizeMediumImage) {
        this.sizeMediumImage = sizeMediumImage;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    public String getRectangle() {
        return rectangle;
    }

    public void setRectangle(String rectangle) {
        this.rectangle = rectangle;
    }

    public String getHexagon() {
        return hexagon;
    }

    public void setHexagon(String hexagon) {
        this.hexagon = hexagon;
    }

    public String getOctagon() {
        return octagon;
    }

    public void setOctagon(String octagon) {
        this.octagon = octagon;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getPieceQuantity() {
        return pieceQuantity;
    }

    public void setPieceQuantity(String pieceQuantity) {
        this.pieceQuantity = pieceQuantity;
    }

    public String getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(String cartStatus) {
        this.cartStatus = cartStatus;
    }

    public String getShowPieceStatus() {
        return showPieceStatus;
    }

    public void setShowPieceStatus(String showPieceStatus) {
        this.showPieceStatus = showPieceStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTimeAdded() {
        return dateTimeAdded;
    }

    public void setDateTimeAdded(String dateTimeAdded) {
        this.dateTimeAdded = dateTimeAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(position);
        dest.writeString(cartId);
        dest.writeString(sizeId);
        dest.writeString(productId);
        dest.writeString(productTitle);
        dest.writeString(catId);
        dest.writeString(catTitle);
        dest.writeString(sizeTitle);
        dest.writeString(weight);
        dest.writeString(priceType);
        dest.writeString(nosKg);
        dest.writeString(price);
        dest.writeString(sizeImage);
        dest.writeString(sizeThumbImage);
        dest.writeString(sizeMediumImage);
        dest.writeString(height);
        dest.writeString(width);
        dest.writeString(round);
        dest.writeString(square);
        dest.writeString(rectangle);
        dest.writeString(hexagon);
        dest.writeString(octagon);
        dest.writeString(quantity);
        dest.writeString(measurement);
        dest.writeString(pieceQuantity);
        dest.writeString(cartStatus);
        dest.writeString(showPieceStatus);
        dest.writeString(status);
        dest.writeString(dateTimeAdded);
    }
}
