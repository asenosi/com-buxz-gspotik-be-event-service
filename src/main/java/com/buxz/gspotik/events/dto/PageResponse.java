package com.buxz.gspotik.events.dto;

import java.util.List;

public class PageResponse<T> {
    private List<T> items;
    private long totalItems;
    private int page;
    private int size;

    public PageResponse() {}

    public PageResponse(List<T> items, long totalItems, int page, int size) {
        this.items = items;
        this.totalItems = totalItems;
        this.page = page;
        this.size = size;
    }

    public List<T> getItems() { return items; }
    public void setItems(List<T> items) { this.items = items; }
    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { this.totalItems = totalItems; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}

