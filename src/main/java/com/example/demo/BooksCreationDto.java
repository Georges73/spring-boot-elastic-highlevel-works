package com.example.demo;

import java.util.List;

public class BooksCreationDto {
	
	private List<Book> books;
	 
    // default and parameterized constructor
	
	
 
    public void addBook(Book book) {
        this.books.add(book);
    }

	public BooksCreationDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BooksCreationDto(List<Book> books) {
		super();
		this.books = books;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
    
    

}
