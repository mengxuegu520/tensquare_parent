package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 *
 * @author Administrator
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    public User findByMobile(String mobile);

    /**
     *      * 更新粉丝数
     *      * @param userid 用户ID
     *      * @param x 粉丝数
     */
    @Modifying
    @Query(value = "update tb_user  set fanscount=fanscount+? where id=?",nativeQuery = true)
    public void updateIncFanscount(Integer  x, String friendid);
    @Modifying
    @Query(value = "update tb_user  set followcount=followcount+? where id=?",nativeQuery = true)
    public void updateIncFollowcount(Integer  x, String userid);
}
