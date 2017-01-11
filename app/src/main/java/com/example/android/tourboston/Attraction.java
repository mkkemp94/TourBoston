package com.example.android.tourboston;

/**
 * Created by kempm on 12/18/2016.
 */

public class Attraction {

    // Variables
    private String mAttractionName;
    private int mImageResource;
    private int mBackgroundResource = -1;
    private String mDescription;

    // Constructor
    public Attraction(String mAttractionName, int mImageResource, String description) {
        this.mAttractionName = mAttractionName;
        this.mImageResource = mImageResource;
        this.mDescription = description;
    }

    // Constructor
    public Attraction(String mAttractionName, int mImageResource, int backgroundResource, String description) {
        this.mAttractionName = mAttractionName;
        this.mImageResource = mImageResource;
        this.mBackgroundResource = backgroundResource;
        this.mDescription = description;
    }

    // Getters
    public String getAttractionName() {
        return mAttractionName;
    }
    public int getImageResource() {
        return mImageResource;
    }
    public int getBackgroundResource() { return mBackgroundResource; }
    public String getDescription() { return mDescription; }

    // Setters
    public void setAttractionName(String mAttractionName) {
        this.mAttractionName = mAttractionName;
    }
    public void setImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    // To string
    @Override
    public String toString() {
        return "Attraction{" +
                "mAttractionName='" + mAttractionName + '\'' +
                ", mImageResource=" + mImageResource +
                ", mBackgroundResource=" + mBackgroundResource +
                ", mDescription=" + mDescription +
                '}';
    }
}
