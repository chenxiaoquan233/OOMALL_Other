package cn.edu.xmu.other.service;


import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.dao.FavoriteDao;
import cn.edu.xmu.other.model.bo.FavoriteBo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jx
 * @version 创建时间：2020/12/3 上午8:57
 */
@Service
public class FavoriteService {
    private static final Logger logger = LoggerFactory.getLogger(FavoriteService.class);

    @Autowired
    private FavoriteDao favoriteDao;

    public ReturnObject<PageInfo<VoObject>> getFavorites(Long userId, int page, int pageSize) {
        return favoriteDao.getFavoritesByUserId(userId,page,pageSize);
    }

    public ReturnObject<VoObject> addFavorites(Long userId, Long spuId) {
        return favoriteDao.addFavorites(userId, spuId);
    }

    public ResponseCode deleteFavorites(Long userId,Long id){
        return favoriteDao.deletefavorites(userId,id);
    }
}
