package cn.uneko.tiktokmagic


/**
 * SIM实体类
 * @param country 国家名称
 * @param alpha   SIM运营商
 * @param abbre   国家缩写
 * @param numeric SIM编号
 */
data class Sim(var country: String, var alpha: String, var abbre: String, var numeric: String)
