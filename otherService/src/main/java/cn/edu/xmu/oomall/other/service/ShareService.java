package cn.edu.xmu.oomall.other.service;

import cn.edu.xmu.oomall.other.dao.FavoriteDao;
import cn.edu.xmu.oomall.other.dao.ShareDao;
import cn.edu.xmu.oomall.other.model.bo.BeSharedBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jx
 * @version 创建时间：2020/12/5 下午2:57
 */
@Service
public class ShareService {
    private static final Logger logger = LoggerFactory.getLogger(ShareService.class);

    @Autowired
    private ShareDao shareDao;


}
