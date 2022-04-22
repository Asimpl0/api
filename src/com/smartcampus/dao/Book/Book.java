package com.smartcampus.dao.Book;

import org.springframework.util.concurrent.ListenableFutureAdapter;

import java.util.List;

public class Book {
    public List<BookType> bookTypes;
    public List<BookInfo> bookInfos;
    public Book() {
    }
    public Book(List<BookType> bookTypes, List<BookInfo> bookInfos) {
        this.bookTypes = bookTypes;
        this.bookInfos = bookInfos;
    }
}
