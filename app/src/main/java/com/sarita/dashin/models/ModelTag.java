package com.sarita.dashin.models;

public class ModelTag {

    private String Image, Tag;

    public ModelTag() {
    }

    @Override
    public String toString() {
        return "ModelTag{" +
                "Image='" + Image + '\'' +
                ", Tag='" + Tag + '\'' +
                '}';
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
