package com.example.demo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/books")
public class BookController {

	private BookDao bookDao;

	// @Autowired
	public BookController(BookDao bookDao) {
		this.bookDao = bookDao;
	}

	@GetMapping("/{id}")
	public Map<String, Object> getBookById(@PathVariable String id) {
		return bookDao.getBookById(id);
	}

	@GetMapping(value = "/title/{title}", produces = "application/json; charset=utf-8")
	public ModelAndView getTitle(@PathVariable String title, Model model) throws IOException {
		System.err.println("----------------------Title-----------------------" + title);
		
		List<Book> booksFound = bookDao.getBookByTitle(title);
		model.addAttribute("books", booksFound);
		ModelAndView mav = new ModelAndView("booksPage");

		return mav;
	}

	@GetMapping(value = "/findAllThyme")
	public ModelAndView getTitleThyme(Model model) throws Exception {

		List<Book> allBooks = bookDao.findAll();

		model.addAttribute("books", allBooks);
		ModelAndView mav = new ModelAndView("booksPage");

		return mav;
	}

	@GetMapping(value = "/findAll", produces = "application/json; charset=utf-8")
	public List<Book> findAll() throws Exception {

		System.err.println("----------------------findall-----------------------");

		return bookDao.findAll();
	}

	@PostMapping
	public Book insertBook(@RequestBody Book book) throws Exception {
		return bookDao.insertBook(book);
	}

	@PutMapping("/{id}")
	public Map<String, Object> updateBookById(@RequestBody Book book, @PathVariable String id) {
		return bookDao.updateBookById(id, book);
	}

	@DeleteMapping("/{id}")
	public void deleteBookById(@PathVariable String id) {
		bookDao.deleteBookById(id);
	}

	@GetMapping("/search")
	public List<Book> search(@RequestParam("query") String query) {
		return bookDao.wildcardQuery(query);
	}

	@GetMapping("/create")
	public String showCreateForm(Model model) {
		BooksCreationDto booksForm = new BooksCreationDto();

		for (int i = 1; i <= 3; i++) {
			booksForm.addBook(new Book());
		}

		model.addAttribute("form", booksForm);
		return "books/createBooksForm";
	}

}