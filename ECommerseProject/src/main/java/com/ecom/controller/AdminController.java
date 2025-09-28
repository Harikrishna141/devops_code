package com.ecom.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.repository.CategoryRepository;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	 	@GetMapping("/")
	public String adminPage(Model model) {
	 		
		 	    // Existing stats
		 	    model.addAttribute("newAccounts", 234);
		 	    model.addAttribute("totalExpenses", 71);
		 	    model.addAttribute("companyValue", "1.45M");
		 	    model.addAttribute("newEmployees", 34);

		 	    // E-commerce stats
		 	    model.addAttribute("totalOrders", 1200);
		 	    model.addAttribute("totalProducts", 350);
		 	    model.addAttribute("totalCustomers", 890);

		 	    // Chart data
		 	    model.addAttribute("trafficData", new int[]{400, 500, 600, 700, 300, 650, 800});
		 	    model.addAttribute("incomePercent", 75);

		return "admin/dashboard";
	

}
	 
	 	/*@GetMapping("/dashboard")
	 	public String dashboard(Model model) {
	 	    // Existing stats
	 	    model.addAttribute("newAccounts", 234);
	 	    model.addAttribute("totalExpenses", 71);
	 	    model.addAttribute("companyValue", "1.45M");
	 	    model.addAttribute("newEmployees", 34);

	 	    // E-commerce stats
	 	    model.addAttribute("totalOrders", 1200);
	 	    model.addAttribute("totalProducts", 350);
	 	    model.addAttribute("totalCustomers", 890);

	 	    // Chart data
	 	    model.addAttribute("trafficData", new int[]{400, 500, 600, 700, 300, 650, 800});
	 	    model.addAttribute("incomePercent", 75);

	 	    return "admin/dashboard";
	 	}*/

	 	
	 	
	 /*	@GetMapping("/dashboard")
	 	public String showDashboard(Model model) {

	 	    // Order Updates
	 	    model.addAttribute("avgOrders", 67843);
	 	    model.addAttribute("shipments", 2342);
	 	    model.addAttribute("returns", 1584);
	 	    model.addAttribute("sales", "1.45M");

	 	    // Performance Details
	 	    model.addAttribute("callsPerHour", "22/25");
	 	    model.addAttribute("refunds", "11/22");
	 	    model.addAttribute("discounts", "04/22");

	 	    // Sales Data (Pie Chart)
	 	    model.addAttribute("salesData", new int[]{25, 24, 30, 11, 10}); 
	 	    model.addAttribute("salesLabels", new String[]{"Apparels", "Electronics", "Furniture", "Groceries", "Others"});

	 	    // Shipping Data (Doughnut Chart)
	 	    model.addAttribute("shippingData", new int[]{32000, 25000, 10000});
	 	    model.addAttribute("shippingLabels", new String[]{"Out for Delivery", "In-Transit", "Ready to Ship"});

	 	    return "admin/dashboard";
	 	}
*/
	 	
	 	
	 	@GetMapping("/loadAddProduct")
		public String loadAddProduct(Model m) {
			List<Category> categories = categoryService.getAllCategory();
			m.addAttribute("categories", categories);
			return "admin/add_product";
		}
	 	
	 	
	 	@PostMapping("/saveProduct")
		public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
				HttpSession session) throws IOException {

			String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

			product.setImage(imageName);
			product.setDiscount(0);
			product.setDiscountPrice(product.getPrice());
			Product saveProduct = productService.saveProduct(product);

			if (!ObjectUtils.isEmpty(saveProduct)) {

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
						+ image.getOriginalFilename());

				 System.out.println(path);
				Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				session.setAttribute("succMsg", "Product Saved Success");
			} else {
				session.setAttribute("errorMsg", "something wrong on server");
			}

			return "redirect:/admin/loadAddProduct";
		}
	 	
	 	
	 	@GetMapping("/products")
		public String loadViewProduct(Model m) {
	 		
	 		m.addAttribute("products", productService.getAllProducts());
		//Model m, @RequestParam(defaultValue = "") String ch,
			//	@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
				//@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

//			List<Product> products = null;
//			if (ch != null && ch.length() > 0) {
//				products = productService.searchProduct(ch);
//			} else {
//				products = productService.getAllProducts();
//			}
//			m.addAttribute("products", products);

		/*	Page<Product> page = null;
			if (ch != null && ch.length() > 0) {
				page = productService.searchProductPagination(pageNo, pageSize, ch);
			} else {
				page = productService.getAllProductsPagination(pageNo, pageSize);
			}
			m.addAttribute("products", page.getContent());

			m.addAttribute("pageNo", page.getNumber());
			m.addAttribute("pageSize", pageSize);
			m.addAttribute("totalElements", page.getTotalElements());
			m.addAttribute("totalPages", page.getTotalPages());
			m.addAttribute("isFirst", page.isFirst());
			m.addAttribute("isLast", page.isLast());
*/
			return "admin/products";
		}
	 	
	 	
	 	@GetMapping("/deleteProduct/{id}")
		public String deleteProduct(@PathVariable int id, HttpSession session) {
			Boolean deleteProduct = productService.deleteProduct(id);
			if (deleteProduct) {
				session.setAttribute("succMsg", "Product delete success");
			} else {
				session.setAttribute("errorMsg", "Something wrong on server");
			}
			return "redirect:/admin/products";
		}

		@GetMapping("/editProduct/{id}")
		public String editProduct(@PathVariable int id, Model m) {
			m.addAttribute("product", productService.getProductById(id));
			m.addAttribute("categories", categoryService.getAllCategory());
			return "admin/edit_product";
		}

		@PostMapping("/updateProduct")
		public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
				HttpSession session, Model m) {

			if (product.getDiscount() < 0 || product.getDiscount() > 100) {
				session.setAttribute("errorMsg", "invalid Discount");
			} else {
				Product updateProduct = productService.updateProduct(product, image);
				if (!ObjectUtils.isEmpty(updateProduct)) {
					session.setAttribute("succMsg", "Product update success");
				} else {
					session.setAttribute("errorMsg", "Something wrong on server");
				}
			}
			return "redirect:/admin/editProduct/" + product.getId();
		}
 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	
	 	@GetMapping("/category")
		//public String category() {
		public String category(Model m 
				, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
				@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
			 m.addAttribute("categorys", categoryService.getAllCategory());
			Page<Category> page = categoryService.getAllCategorPagination(pageNo, pageSize);
			List<Category> categorys = page.getContent();
			m.addAttribute("categorys", categoryService.getAllCategory());

			m.addAttribute("pageNo", page.getNumber());			
			m.addAttribute("pageSize", pageSize);
			m.addAttribute("totalElements", page.getTotalElements());
			m.addAttribute("totalPages", page.getTotalPages());
			m.addAttribute("isFirst", page.isFirst());
			m.addAttribute("isLast", page.isLast());

			return "admin/category";
		}	
		
	 	
	 	@GetMapping("/loadEditCategory/{id}")
		public String loadEditCategory(@PathVariable int id, Model m) {
			m.addAttribute("category", categoryService.getCategoryById(id));
			return "admin/edit_category";
		}
	 	
	 	
	 	@PostMapping("/updateCategory")
		public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
				HttpSession session) throws IOException {

			Category oldCategory = categoryService.getCategoryById(category.getId());
			String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

			if (!ObjectUtils.isEmpty(category)) {

				oldCategory.setName(category.getName());
				oldCategory.setIsActive(category.getIsActive());
				oldCategory.setImageName(imageName);
			}

			Category updateCategory = categoryService.saveCategory(oldCategory);

			if (!ObjectUtils.isEmpty(updateCategory)) {

				if (!file.isEmpty()) {
					File saveFile = new ClassPathResource("static/img").getFile();

					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
							+ file.getOriginalFilename());

					// System.out.println(path);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				}

				session.setAttribute("succMsg", "Category update success");
			} else {
				session.setAttribute("errorMsg", "something wrong on server");
			}

			return "redirect:/admin/loadEditCategory/" + category.getId();
		}
		
	 	
	 	@GetMapping("/deleteCategory/{id}")
		public String deleteCategory(@PathVariable int id, HttpSession session) {
			Boolean deleteCategory = categoryService.deleteCategory(id);

			if (deleteCategory) {
				session.setAttribute("succMsg", "category delete success");
			} else {
				session.setAttribute("errorMsg", "something wrong on server");
			}

			return "redirect:/admin/category";
		}
		
		
		
		
		@PostMapping("/saveCategory")
		public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
				HttpSession session) throws IOException {

			String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
			category.setImageName(imageName);

			Boolean existCategory = categoryService.existCategory(category.getName());

			if (existCategory) {
				session.setAttribute("errorMsg", "Category Name already exists");
			} else {

				Category saveCategory = categoryService.saveCategory(category);

				if (ObjectUtils.isEmpty(saveCategory)) {
					session.setAttribute("errorMsg", "Not saved ! internal server error");
				} else {

					File saveFile = new ClassPathResource("static/img").getFile();

					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
							+ file.getOriginalFilename());

					 System.out.println(path);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

					session.setAttribute("succMsg", "Saved successfully");
				}
			}

			return "redirect:/admin/category";
		}
		
		
}