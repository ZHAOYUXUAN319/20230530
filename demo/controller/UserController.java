package com.example.demo.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.entity.Buken;
import com.example.demo.entity.User;
import com.example.demo.obj.BukenDto;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BukenService;

import jakarta.servlet.http.HttpSession;

/**
 * ユーザー情報 Controller
 */
@Controller
@SessionAttributes("isVip")
public class UserController {

	/**
	 * ユーザー情報 Service
	 */
	//@Autowired
	//private UserService userService;

	/**
	 * ユーザー情報一覧画面を表示
	 * 
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */

//	@GetMapping(value = "/user/list")
//	public ModelAndView displayList(Model model) {
//		List<UserDto> userlist = userService.searchAll();
//		System.out.println("User情報取得しました。" + userlist);
//		model.addAttribute("userlist", userlist);
//
//		ModelAndView maView = new ModelAndView("/user/list");
//		return maView;
//	}

	// 物件公開一覧
	@Autowired
	private BukenService bukenService;

	@GetMapping(value = "/user/Buken")
	public ModelAndView displayListBuken(Model model) {
		List<BukenDto> bukenList = bukenService.searchAll();
		System.out.println("物件情報取得しました。" + bukenList);
		model.addAttribute("bukenList", bukenList);

		ModelAndView modelAndView = new ModelAndView("/user/Buken");
		return modelAndView;
	}

	// 物件非公開一覧

	@GetMapping(value = "/user/BukenHikoukei")
	public ModelAndView displayListBukenhikoukei(Model model) {
		List<BukenDto> bukenList = bukenService.searchBukenHikoukei();
		System.out.println("物件情報取得しました。" + bukenList);
		model.addAttribute("bukenList", bukenList);

		ModelAndView modelAndView = new ModelAndView("/user/Buken");
		return modelAndView;
	}

	/**
	 * ユーザー新規登録画面を表示
	 * 
	 * @param model Model
	 * @return ユーザー情報一覧画面
	 */
	@GetMapping(value = "/user/add")
	public String displayAdd(Model model) {
		return "user/add";
	}

	/**
	 * ユーザー情報詳細画面を表示
	 * 
	 * @param id    表示するユーザーID
	 * @param model Model
	 * @return ユーザー情報詳細画面
	 */
	@GetMapping("/user/{id}")
	public String displayView(@PathVariable Long id, Model model) {
		return "user/view";
	}

	// 物件新規画面
	@GetMapping(value = "/user/BukenAdd")
	public String displayBukenAdd(Model model) {
		return "/user/BukenAdd";
	}

//物件新規
	@PostMapping("/user/BukenAdd")
	public String addBuken(@ModelAttribute BukenDto bukenDto) {
		Buken buken = new Buken();
		// buken.setPropertyId(bukenDto.getPropertyId());
		buken.setPropertyName(bukenDto.getPropertyName());
		buken.setAddress(bukenDto.getAddress());
		buken.setPropertyType(bukenDto.getPropertyType());
		buken.setPeriod(bukenDto.getPeriod());
		buken.setPropertyArea(bukenDto.getPropertyArea());
		buken.setPrice(bukenDto.getPrice());
		buken.setSyozokuCompanyId(bukenDto.getSyozokuCompanyId());
		buken.setStatus(bukenDto.getStatus());

		bukenService.saveBuken(buken);

		return "redirect:/user/Buken";
	}

	@GetMapping("/user/home")
	public String showMyPage() {
		return "user/home";
	}

	// 删除
	@PostMapping("/user/Buken/delete/{id}")
	public String deleteBuken(@PathVariable("id") Long propertyId) {
		// bukenService.deleteBukenById(propertyId);
		bukenService.deleteBuken(propertyId);
		return "redirect:/user/Buken";
	}

	// 编辑物件
	@GetMapping("/user/BukenEdit/{propertyId}")
	public String displayBukenEdit(@PathVariable Long propertyId, Model model) {
		BukenDto bukenDto = bukenService.getBukenById(propertyId);
		model.addAttribute("bukenDto", bukenDto);
		return "user/BukenEdit";
	}

	// 更新物件
	@PostMapping("/user/BukenUpdate")
	public String updateBuken(@ModelAttribute("bukenDto") BukenDto bukenDto) {
		bukenService.updateBuken(bukenDto);
		return "redirect:/user/BukenList";
	}

	// 検索
	// 构造函数注入BukenService
	public UserController(BukenService bukenService) {
		this.bukenService = bukenService;
	}

	@PostMapping("/submit")
	public ModelAndView submitForm(@RequestParam("uid") Long propertyId) {
		List<Buken> bukenList = bukenService.searchByUidValue(propertyId);

		ModelAndView modelAndView = new ModelAndView("user/Buken");
		modelAndView.addObject("bukenList", bukenList);
		return modelAndView;
	}

	// 期間検索
	@PostMapping("/submitkikan")
	public ModelAndView submitForm1(@RequestParam("fromdate") Date fromdate, @RequestParam("todate") Date todate) {
		List<Buken> bukenList = bukenService.searchByPeriod(fromdate, todate);

		ModelAndView modelAndView = new ModelAndView("user/Buken");
		modelAndView.addObject("bukenList", bukenList);
		return modelAndView;
	}

	@Autowired
	private HttpSession session;

	@GetMapping("/login")
	public String showLoginForm() {
	    return "user/login";
	}

//	@PostMapping("/login")
//	public String login(@RequestParam("id") String id, @RequestParam("password") String password, Model model) {
//	    // 执行登录验证逻辑，验证用户ID和密码是否正确
//
//	    // 假设验证成功并判断用户为VIP
//	    boolean vipStatus = true;
//
//	    // 将VIP状态存储在session中
//	    session.setAttribute("vipStatus", vipStatus);
//		System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
//	    return "redirect:/user/home";
//	}
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
	    // 执行登录验证逻辑，验证用户名和密码是否正确
	    User user = userRepository.findByUsername(username); // 通过用户名查询用户
	    System.out.println(username);

	    if (user != null && user.getPassword().equals(password) && "VIP".equals(user.getStatus())) {
	        // 验证通过，假设用户为VIP
	        boolean isVip = true;

	        // 将VIP状态存储在session中
	        session.setAttribute("isVip", isVip);

	        System.out.println("Login successful");
	        return "redirect:/user/Buken";
	    } else {
	        System.out.println("Login failed");
	        return "redirect:/login?error";
	    }

	}

}