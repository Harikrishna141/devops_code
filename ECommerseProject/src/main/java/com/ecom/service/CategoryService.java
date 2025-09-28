package com.ecom.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ecom.model.Category;

public interface CategoryService {

	public Category saveCategory(Category category);

	public Boolean existCategory(String name);

	public List<Category> getAllCategory();

	public Boolean deleteCategory(int id);

	public Category getCategoryById(int id);
	//public Category getCategoryByName(String name);

	public List<Category> getAllActiveCategory();

	Page<Category> getAllCategorPagination(Integer pageNo, Integer pageSize);

	//public Page<Category> getAllCategorPagination(Integer pageNo,Integer pageSize);

}
