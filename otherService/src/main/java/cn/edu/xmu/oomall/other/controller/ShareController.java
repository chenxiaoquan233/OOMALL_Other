package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.oomall.other.service.ShareService;
import cn.edu.xmu.oomall.other.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午2:57
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ShareController {
    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private ShareService shareService;




}
