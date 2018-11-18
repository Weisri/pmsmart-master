package com.pm.intelligent;

/**
 * Created by WeiSir on 2018/7/3.
 */

public class Contants {

    /**
     * 正式服
     */
    private static final String IP = "https://pmade.cn";
    private static final String PORT = "10002";

    /**
     * 本地
     */
//    private static final String IP = "http://192.168.1.110";
//    private static final String PORT = "8080";

    public static final String SERVER_URL = IP + ":" + PORT;


 /**
  * 加热座椅温度
  */
 public static String BASE_URL2 = "http://47.99.157.232:10005/controlApp/heatedSeat?";
// public static String BASE_URL2 = "http://192.168.1.110:8080/controlApp/heatedSeat?";


    /**
     * 登录接口
     * ok
     */
    public static final String LOGIN = SERVER_URL + "/controlApp/loginApp?";
    /**
     * 盒子报告
     * ok
     */
    public static final String BOX = SERVER_URL + "/controlApp/doGetBoxReport?";

    /**
     * 系统报告
     * ok
     */
    public static final String SYSTEM = SERVER_URL + "/controlApp/getSystem?";
    /**
     * 首页站台
     * ok
     */
    public static final String sheter = SERVER_URL + "/controlApp/getShelterName?";
    /**
     * 故障跟踪
     * ok
     */
    public static final String FALSE = SERVER_URL + "/controlApp/getTroubleInfo?";

    /**
     * 报警信息
     * ok
     */
    public static final String ALARM = SERVER_URL + "/controlApp/getAlarms?";

    /**
     * 情景智能
     * ok
     */
    public static final String SCENES = SERVER_URL + "/controlApp/getScenes?";
    /**
     * 智能调节状态
     * ok
     */
    public static final String ADJUEST = SERVER_URL + "/controlApp/getAdjusts?";

    /**
     * 获取智能调节状态
     * ok
     */
    public static final String SWITCHS = SERVER_URL + "/controlApp/getSwitchs?";


    /**
     * 硬件报告
     * ok
     */
    public static final String HARDWARES = SERVER_URL + "/controlApp/getHadReportInfo?";

    /**
     * 月用电量
     */
    public static final String ElectricMonth = SERVER_URL + "/controlApp/getElectricMonth?";

    /**
     * 周用电量
     */
    public static final String ElectricWeek = SERVER_URL + "/controlApp/getElectricWeek?";


    public static int[] menuIcons = {
            R.mipmap.home_menu_box,
            R.mipmap.home_menu_hard,
            R.mipmap.home_menu_system,
            R.mipmap.home_menu_control,
            R.mipmap.home_menu_weather,
            R.mipmap.home_menu_electricity,
            R.mipmap.home_menu_alarm,
            R.mipmap.home_menu_scene,
            R.mipmap.home_menu_tracking,
//            R.mipmap.home_menu_abouts,
    };

    public static String[] menuNames = {
            "盒子报告",
            "硬件报告",
            "系统报告",
            "智能控制",
            "气象检测",
            "用电监测",
            "实时报警",
            "情景智能",
            "异常跟踪",
//            "关于我们"
    };


}
