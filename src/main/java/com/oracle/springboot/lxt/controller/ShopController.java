package com.oracle.springboot.lxt.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oracle.springboot.lxt.po.Shop;
import com.oracle.springboot.lxt.service.ShopService;
import com.oracle.springboot.lxt.util.OSSClientUtil;
import com.oracle.springboot.lxt.util.SnowFlakeUtil;
import com.oracle.springboot.lxt.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RequestMapping("/shop")
@Controller
public class ShopController {
    @Autowired
    private ShopService shopService;

    @RequestMapping("/image")
    public String image(){
        return "image";
    }


    @RequestMapping("/getshoplist")
    public String getuserslist(HttpServletRequest request, @ModelAttribute("shop") Shop shop, @RequestParam(value = "page",defaultValue = "1")int currentPage,@RequestParam(value = "min",defaultValue = "0") int min, @RequestParam(value = "max",defaultValue = "1000000") int max){
        PageHelper.startPage(currentPage,5);
        List<Shop> userList=shopService.getshoplist(shop,min,max);
        PageInfo pageInfo=new PageInfo(userList);
        request.setAttribute("pageInfo",pageInfo);
        request.setAttribute("shop",shop);
        request.setAttribute("min",min);
        request.setAttribute("max",max);
        return "shoplist";
    }

@RequestMapping("/toadd")
    public String toadd(){
        return "add";
    }
    @RequestMapping("/add")
    public String add(Shop shop, @RequestParam("file") MultipartFile file) throws Exception {
        SnowFlakeUtil snowFlakeUtil=new SnowFlakeUtil(5,5);
        shop.setId(snowFlakeUtil.nextId());
        Integer num=shop.getNum();
        shop.setNum(num);
        shop.setStatus("1");
        OSSClientUtil ossClientUtil=new OSSClientUtil();
        String s = ossClientUtil.uploadImg2Oss(file);
        String url=ossClientUtil.getUrl(s);
        shop.setUrl(url);
        int i=shopService.insert(shop);

        if (i>0){
            return "redirect:/shop/getshoplist";
        }
        return "shop/getshoplist";
    }
    @RequestMapping("/checkform")
    public @ResponseBody
    ResponseVo checkform(HttpServletRequest request, HttpSession session, @RequestParam("file") MultipartFile file) throws Exception {

        ResponseVo<String> responseVo=new ResponseVo();
        String filename= file.getOriginalFilename();
        OSSClientUtil ossClientUtil=new OSSClientUtil();
        String s = ossClientUtil.uploadImg2Oss(file);
        String url=ossClientUtil.getUrl(s);
        System.out.println(url);
        responseVo.setT("url");
        responseVo.setCode("200");
        return responseVo;
    }
@RequestMapping("/delete")
    public String delete(Long[] id){
        int a=0;
    System.out.println(id);
    for (Long l: id) {
        a=shopService.updatestatus(l);
    }

        return "redirect:/shop/getshoplist";
    }
    @RequestMapping("/toupdate")
    public String toupdate(Long id,HttpServletRequest request){
        Shop shop=shopService.selectByPrimaryKey(id);
        request.setAttribute("shop",shop);
        return "update";
    }
    @RequestMapping("/update")
    public String update(Shop shop, @RequestParam("file") MultipartFile file) throws Exception {
        System.out.println(shop);
        OSSClientUtil ossClientUtil=new OSSClientUtil();
        String s = ossClientUtil.uploadImg2Oss(file);
        String url=ossClientUtil.getUrl(s);
        shop.setUrl(url);

        int i=shopService.updateByPrimaryKeySelective(shop);
        if (i>0){
            return "redirect:/shop/getshoplist";
        }
        return "redirect:/shop/getshoplist";
    }
}
