package com.tensquare.friend.service;

import com.tensquare.friend.Dao.FriendDao;
import com.tensquare.friend.Dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendService {
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;

    @Transactional
    public int addFriend(String userid, String friendid) {
        //判断如果用户已经添加了这个好友，则不进行任何操作,返回0
        if (friendDao.selectCount(userid, friendid) > 0) {
            return 0;
        }
        //向喜欢表中添加记录
        Friend friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");  //第一次默认单项喜欢
        friendDao.save(friend);
        //判断对方是否喜欢你，如果喜欢，将islike设置为1
        if (friendDao.selectCount(friendid, userid) > 0) {
            friendDao.updateLike("1", userid, friendid);
            friendDao.updateLike("1", friendid, userid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        //先判断是否是 非好友  如果是 不做任何操作
        if (friendDao.selectCount(userid, friendid) > 0) {
            return 0;
        }
        //向不喜欢表中添加记录
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
        return 1;
    }

    /**
     *      * 删除好友
     *      * @param userid
     *      * @param friendid
     *      
     */
    @Transactional
    public void deleteFriend(String userid, String friendid) {
        friendDao.deleteFriend(userid, friendid);
        friendDao.updateLike("0", friendid, userid);
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }
}
