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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.Category;
import com.ecom.model.Product;
import com.ecom.model.UserDtls;
import com.ecom.service.CategoryService;
import com.ecom.service.ProductService;
import com.ecom.service.UserService;

import jakarta.servlet.http.HttpSession;



@Controller
public class HomeController {
	@Autowired
	private ProductService productService;
	
	

	@Autowired
	private CategoryService categoryService;

	
		@Autowired
	private UserService userService;
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/signin")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage(HttpSession session, Model model) {
        Object succMsg = session.getAttribute("succMsg");
        Object errorMsg = session.getAttribute("errorMsg");

        model.addAttribute("succMsg", succMsg);
        model.addAttribute("errorMsg", errorMsg);

        // clear from session after reading
        session.removeAttribute("succMsg");
        session.removeAttribute("errorMsg");

        return "register";
    }
    
    
    @GetMapping("/index")
    public String adminPage() {
        return "index"; 
    
}
    
    
//    @PostMapping("/saveuser")
//    public String saveuser(@ModelAttribute UserDtls user,@RequestParam("img") MultipartFile file) {
//    	String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
//		//user.setProfileImage(imageName);
//    	userService.saveUser(user);
//        return "redirect:/register"; 
//    }
//    
    
    
    
    
    @PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session)
			throws IOException {

		//Boolean existsEmail = userService.existsEmail(user.getEmail());

		//if (existsEmail) {
			//session.setAttribute("errorMsg", "Email already exist");
		//} else {
			String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
			//user.setProfileImage(imageName);
			UserDtls saveUser = userService.saveUser(user);

			if (!ObjectUtils.isEmpty(saveUser)) {
				if (!file.isEmpty()) {
					File saveFile = new ClassPathResource("static/img").getFile();

					Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile_img" + File.separator
							+ file.getOriginalFilename());

				//System.out.println(path);
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				}
				session.setAttribute("succMsg", "Register successfully");
			} else {
				session.setAttribute("errorMsg", "something wrong on server");
			}
		

		return "redirect:/register";
	}
    
    
    
  /*  @GetMapping("/products")
    public String about(Model model) {
		
		//model.addAttribute("products", "active");
		return "product";
	} */
    
   // @GetMapping("/product")
    //public String product() {
		//return "view_product";
	//} 
    
    
    
    
	@GetMapping("/products")
	public String loadViewProduct(Model m, @RequestParam(value = "category", defaultValue = "") String category)
		{
        System.out.println("category : "+category);
		List<Category> categories = categoryService.getAllActiveCategory();
 		
 		m.addAttribute("products", productService.getAllProducts());
    
 		m.addAttribute("categories", categories);
 		
 		List<Product> products = productService.getAllActiveProducts(category);
	m.addAttribute("products", products);
 		
 		
 	

		
 		return "product";
	}
 	
    
	@GetMapping("/product/{id}")
	public String product(@PathVariable int id, Model m) {
		Product productById = productService.getProductById(id);
		m.addAttribute("product", productById);
		return "view_product";
	}
 
    
    
    
}
