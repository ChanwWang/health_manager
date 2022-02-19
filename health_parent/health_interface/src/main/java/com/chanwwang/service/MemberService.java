package com.chanwwang.service;

import com.chanwwang.pojo.Member;

import java.util.List;

/**
 * @author ChanwWang
 * @version 1.0
 */
public interface MemberService {

    //根据手机号查询会员
    public Member findByTelephone(String telephone);

    public void add(Member member);
    public List<Integer> findMemberCountByMonths(List<String> months);
}
