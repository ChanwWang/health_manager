package com.chanwwang.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chanwwang.dao.MemberDao;
import com.chanwwang.pojo.Member;
import com.chanwwang.service.MemberService;
import com.chanwwang.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ChanwWang
 * @version 1.0
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        if(member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {//格式2022.01
        ArrayList<Integer> list = new ArrayList<>();
        for (String month : months) {
            month += ".31";//格式 2022.01.31
            Integer memberCount = memberDao.findMemberCountBeforeDate(month);
            list.add(memberCount);

        }
        return list;
    }
}
