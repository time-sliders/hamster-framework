package com.noob.storage;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * app首页的一些代码配置
 *
 * @author LuYun
 * @version app 7.0.0
 * @since 2018.05.29
 */
public class AppHomeConfig {

    public static void main(String[] args) {
        String entranceJsonCfg = "[{\"contentUrl\":\"tbj://views/allprodlist?code=tz_plan\",\"imageUrl\":\"https://res.tongbanjie.com/resources/home/icon_home_quickinvest.png\",\"title\":\"快速投资\"},{\"contentUrl\":\"https://pages.tongbanjie.com/ftp/hong/page/hyrE.html\",\"imageUrl\":\"https://res.tongbanjie.com/resources/home/icon_home_invitation.png\",\"title\":\"邀请有礼\",\"endTime\":\"2018-10-30 23:59:59\"},{\"contentUrl\":\"https://pages.tongbanjie.com/ftp/hong/page/hguE.html\",\"imageUrl\":\"https://res.tongbanjie.com/resources/home/icon_home_invitation.png\",\"title\":\"邀请有礼\",\"startTime\":\"2018-10-30 23:59:59\"},{\"contentUrl\":\"https://pages.tongbanjie.com/ftp/hong/page/hqhE.html\",\"imageUrl\":\"https://res.tongbanjie.com/resources/home/icon_home_campaign.png\",\"title\":\"活动推荐\"}]";
        List<HomeEntranceCfg> voList = JSON.parseArray(entranceJsonCfg, HomeEntranceCfg.class);
        System.out.println(voList);
    }


}
