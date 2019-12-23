package com.tensquare.friend.Dao;

import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NoFriendDao extends JpaRepository<NoFriend, String> {
    //  public  Friend findByUseridAndFriendid(String userid,String friendid);
    /**
     *      * 根据用户ID与被关注用户ID查询记录个数
     *      * @param userid
     *      * @param friendid
     *      * @return
     */
    @Query(value = "select count(*) from tb_nofriend  where userid=? and friendid=?", nativeQuery = true)
    public int selectCount(String userid, String friendid);



}
