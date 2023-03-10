package com.example.finalpro.controller;

import com.example.finalpro.dao.CustomerDAO;
import com.example.finalpro.dao.DrawDAO;
import com.example.finalpro.dao.SeatDAO;
import com.example.finalpro.db.DBManager;
import com.example.finalpro.entity.Customer;
import com.example.finalpro.service.CategoryService;
import com.example.finalpro.service.CustomerService;
import com.example.finalpro.service.TicketService;
import com.example.finalpro.util.SendMessage;
import com.example.finalpro.vo.CustomerVO;
import com.example.finalpro.vo.DrawVO;
import com.example.finalpro.vo.MyDrawVO;
import com.example.finalpro.vo.TicketVO;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Setter
public class CustomerController {

    @Autowired
    private CustomerDAO customerDAO;

    static String code;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerDAO dao;

    @Autowired
    private CategoryService ts;

    @Autowired
    private CustomerService cs;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private DrawDAO drawDAO;

    @Autowired
    private SeatDAO seatDAO;


    //public void setDao(CustomerDAO dao){ this.dao = dao; }

    @RequestMapping("/FindCustomer")
    @ResponseBody
    public CustomerVO findCustomer(String custid){
        return DBManager.findByCustid(custid);
    }

    //?????????
    @GetMapping("/login")
    public void login() {
    }

    //????????? ??????
    @GetMapping("/loginFailed")
    public void loginFailed(){
    }

    //????????????
    @GetMapping("/signUp")
    public void signUp() {
    }

    //??????
    @GetMapping("/")
    public String home() {
        return "/main";
    }

    //?????? ????????? ?????? ????????? ??????
    @GetMapping("/main")
    public ModelAndView main(HttpSession session, Model m){
        ModelAndView mav = new ModelAndView("/main");
        //?????????(????????????) ????????? ????????? ???????????? ?????????
        //??????????????? ??????????????? ??????.
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        //??? ??????????????? ????????? ?????????(????????????) User????????? ?????? ??????.

        System.out.println(authentication.getPrincipal());

        if(!authentication.getPrincipal().equals("anonymousUser")){
            User user = (User) authentication.getPrincipal();
            //??? ????????? User??? ????????? ???????????? ????????? ???????????? ????????????.

            String id = user.getUsername();
            //????????? ????????? ????????? ???????????? ??????.
            //??????, id??? ????????? ???????????? ????????? ??????????????? ??????????????? dao??? ?????? ?????? ????????? ???????????? ????????????

            session.setAttribute("id", id);
            System.out.println("session id = " + session.getAttribute("id"));
            m.addAttribute("id", id);
        }else{
            session.removeAttribute("id");
            m.addAttribute("id","none");
        }
        return mav;
    }

    @GetMapping("/myPage")
    public String myPage(HttpSession session, Model m) {
        System.out.println((String) session.getAttribute("id"));
        String id = (String) session.getAttribute("id");
        if(id == null){
            return "/login";
        }
        Optional<Customer> c = customerDAO.findById(id);
        System.out.println(c.get());
        m.addAttribute("id",c.get());
        return "myPage/myPage";
    }

    @PostMapping("/myPage")
    public String updateCustomer(CustomerVO c, HttpSession session, Model m){
        System.out.println("???????????? ???????????? ??????:"+c);
        c.setPwd(passwordEncoder.encode(c.getPwd()));
        System.out.println("????????? : "+c);
        c.setRole("customer");

        try{
            DBManager.updateCustomer(c);
            System.out.println("sessionId = "+session.getAttribute("id"));
            myPage(session,m);
        }catch (Exception e){

        }
        return "myPage/myPage";
    }


    @GetMapping("/myPageDraw")
    public String myPageDraw(HttpSession session, Model m){
        String custid = (String)session.getAttribute("id");

        List<MyDrawVO> myDraw = new ArrayList<>();
        TicketVO myTicket = null;

        List<DrawVO> list = DBManager.findByDrawCustid(custid);

        for(DrawVO d : list) {
            MyDrawVO md = new MyDrawVO();
            myTicket = DBManager.findByTicketid(d.getTicketid());
            md.setCustid(d.getCustid());
            md.setDrawid(d.getDrawid());
            md.setDrawid(d.getDrawid());
            md.setSeatid(d.getSeatid());
            md.setTicketid(d.getTicketid());
            md.setImg_fname(myTicket.getImg_fname());
            md.setLoc(myTicket.getLoc());
            md.setTicket_date(myTicket.getTicket_date());
            md.setTicket_name(myTicket.getTicket_name());

            if(d.getSeatid() != 0){
                md.setSeatname(seatDAO.findById(d.getSeatid()).get().getSeatname());
            }else{
                md.setSeatname("none");
            }

            System.out.println(md);
            myDraw.add(md);
        }

        m.addAttribute("list",myDraw);

        return "myPage/myPageDraw";
    }

    @GetMapping("/test")
    public String test(){
        return "list";
    }

    @GetMapping("/myPageBook")
    public String myPageBook(HttpSession session) {
        String custid = (String)session.getAttribute("id");

        return "myPage/myPageBook";}

//    @GetMapping("/myPageReview")
//    public String myPageReview() { return "myPage/myPageReview";}

    @PostMapping("/signUp")
    public ModelAndView signUpSubmit(Customer c) {
        System.out.println("customer:"+c);
//		String encPwd = passwordEncoder.encode(m.getPwd());
//		m.setPwd(encPwd);
        ModelAndView mav = new ModelAndView("redirect:/login");
        System.out.println(c.getPwd());
        c.setPwd(passwordEncoder.encode(c.getPwd()));
        System.out.println("customer = " + c);
        c.setRole("customer");
        try {
            System.out.println(c);
            customerDAO.save(c);
            mav.setViewName("/login");
        } catch (Exception e) {
            mav.addObject("msg", "??????????????? ?????????????????????.");
            mav.setViewName("/error");
        }

		/*
		memberDAO.save(m);
		Optional<Member> obj = memberDAO.findById(m.getId());
		if(obj.isEmpty()) {
			mav.addObject("msg", "??????????????? ?????????????????????.");
			mav.setViewName("error");
		}*/
        return mav;
    }

    //????????? ?????? ?????? ?????????
    @GetMapping("/ConfirmCustomerId")
    @ResponseBody
    public int confirmCustomerId(String custid){
        int answer = 0;
        if(customerDAO.findById(custid).isPresent()){
            answer=1;
        }
        return answer;
    }

    //???????????? ?????? ?????? ?????????
    @GetMapping("/ConfirmCustomerPhone")
    @ResponseBody
    public int confirmCustomerPhone(String phone, HttpSession session){
        int answer = 0;
        String myPhone = "none";
        if(session.getAttribute("id")!=null){
            String id = (String)session.getAttribute("id");
            myPhone = customerDAO.findById(id).get().getPhone();
        }

        System.out.println("myPhone:"+myPhone);

        System.out.println(!myPhone.equals(phone));
        System.out.println(phone);
        if(customerDAO.findByPhone(phone) != null ){
            answer=1;
        }
        if(myPhone.equals(phone)){
            answer = 0;
        }
        return answer;
    }

    @GetMapping("/CustomerPhoneAuthentication")
    @ResponseBody
    public int customerPhoneAuthentication(String phoneCode){
        System.out.println(this.code);
        System.out.println("code:"+phoneCode);
        int answer = 0;
        if(!this.code.equals(phoneCode)){
            answer = 1;
        }
        System.out.println(answer);
        return answer;
    }

    @GetMapping("/sendMessage")
    @ResponseBody
    public String sendMessage(String phone){
        System.out.println("phone:"+phone);
        code = SendMessage.sendCodePhone(phone);
        System.out.println("code:"+code);

//        MessageController ms = new MessageController();
//        code = ms.sendCodePhone(phone);
        return code;
    }

    //????????? ??????
    @RequestMapping("/findCustidForm")
    public String findCustidForm(){
        return "/customer/findCustid.html";
    }

    @RequestMapping("/findCustid")
    @ResponseBody
    public CustomerVO findCustid(String name, String phone){
        System.out.println("??????"+name);
        System.out.println("??????"+phone);
        CustomerVO c = DBManager.findCustid(name, phone);
        System.out.println("????????? ????????? ??????"+c);
        return c;
    }

    //??????????????? ???????????? ??????
    @RequestMapping("/findPwdForm")
    public String checkByPhoneForm(){
        return "/customer/findPwd.html";
    }

    @RequestMapping("/checkByPhone")
    @ResponseBody
    public CustomerVO checkByPhone(String custid, String phone){
        System.out.println("?????????"+custid);
        System.out.println("??????"+phone);
        CustomerVO c = DBManager.checkByPhone(custid, phone);
        System.out.println("????????? ????????? ??????"+c);
        return c;
    }

    //??????????????? ???????????? ?????????
    @RequestMapping("/updatePwdbyPhone")
    @ResponseBody
    public String updatePwdbyPhone(CustomerVO c){
        System.out.println("?????????"+c.getCustid());
        System.out.println("??????"+c.getPhone());
        c.setPwd(passwordEncoder.encode(c.getPwd()));
        System.out.println("?????????:"+c );

        try{
            DBManager.updatePwdbyPhone(c);
        }catch(Exception e){
            System.out.println("????????????:"+e.getMessage());
        }
        return "OK";
    }

    //???????????? ???????????? ??????
    @RequestMapping("/checkByEmailForm")
    public String checkByEmailForm(){
        return "/customer/findPwd.html";
    }

    @RequestMapping("/checkByEmail")
    @ResponseBody
    public CustomerVO checkByEmail(String custid, String email){
        System.out.println("?????????"+custid);
        System.out.println("??????"+email);
        CustomerVO c = DBManager.checkByEmail(custid, email);
        System.out.println("????????? ????????? ??????"+c);
        return c;
    }

    @GetMapping("/CustomerEmailAuthentication")
    @ResponseBody
    public int customerEmailAuthentication(String emailCode){
        System.out.println(this.code);
        System.out.println("code:"+emailCode);
        int answer = 0;
        if(!this.code.equals(emailCode)){
            answer = 1;
        }
        System.out.println(answer);
        return answer;
    }

    //???????????? ???????????? ?????????
    @RequestMapping("/updatePwdbyEmail")
    @ResponseBody
    public String updatePwdbyEmail(CustomerVO c){
        System.out.println("?????????"+c.getCustid());
        System.out.println("?????????"+c.getEmail());
        c.setPwd(passwordEncoder.encode(c.getPwd()));
        System.out.println("?????????:"+c );

        try{
            DBManager.updatePwdbyEmail(c);
        }catch(Exception e){
            System.out.println("????????????:"+e.getMessage());
        }
        return "OK";
    }
}