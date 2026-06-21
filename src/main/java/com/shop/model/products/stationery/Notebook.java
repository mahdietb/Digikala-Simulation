package com.shop.model.products.stationery;


public class Notebook extends Stationery {
    private int pageCount;
    private String paperType;

    public Notebook(String name, double price, int stock,
           String manufacturingCountry, int pageCount, String paperType) {
        super(name, price, stock, manufacturingCountry);
        setPageCount(pageCount);
        setPaperType(paperType);
    }

    public int getPageCount() { return pageCount; }

    public void setPageCount(int pageCount) {
        if (pageCount <= 0) throw new IllegalArgumentException("Page count must be positive.");
        this.pageCount = pageCount;
    }

    public String getPaperType() { return paperType; }

    public void setPaperType(String paperType) {
        if (paperType == null || paperType.trim().isEmpty())
            throw new IllegalArgumentException("Paper type cannot be empty.");
        this.paperType = paperType.trim();
    }

    @Override
    public String getSpecificDetails() {
        return getStationeryDetails() +
                "Page count: " + pageCount + "\n" +
                "Paper type: " + paperType + "\n";
    }

    @Override
    public String toString() {
        return "[Notebook] " + super.toString();
    }
}
