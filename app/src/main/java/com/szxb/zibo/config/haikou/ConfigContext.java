package com.szxb.zibo.config.haikou;

/**
 * 用于设置APP配置中的常量
 */
public  class ConfigContext {

    //私钥
    public static final String private_key = "MIICXAIBAAKBgQCrnFUPueWNY3HLcVf55kXzJDb+ftYINmhde+4EMbKjPY38xaZQ\n" +
            "k+OjeXykbo8XgIi/xBpRvogWyOwZKOr4kdnV/PdLSoXCrr3DoTRU9INFiOKZPxFY" +
            "8nYmH6KI4c/z5ooeats8+1bwN5lZdXwXWL/MJA7JrSSSUt0qCwy9MI7+OQIDAQAB" +
            "AoGAIL37HL0DJy7KD17Ywj1FK1bFh1j7zSVUVEHI79PrmWmtJYUwbj9JN29+cIEH" +
            "nBxR+wSXYPFRVceQBFziN/rb7MAS0qNmBxcSzJfqjenoHPZa9smZXpX6W1zHuFTd" +
            "IloV8juM7ssQyRNRNLSIDs2zZBNXHV6eDqW0cdIJuWaKyYECQQDTkZpgv6531pby" +
            "trtWrdgIIjC55YsLZKWv3VqCfvHbhodETA+1EL9y/BV0F0yXE8oDlMbIR99uuU4X" +
            "24/q93mlAkEAz6Z+1KGqy2twmQ1JRO/8B4zfqgUlitYu41dWu+iHDfTC2ex84BRQ" +
            "dXVND2HGiz/fRB3yubc/WAnToLv/kdTGBQJAcDQnQKpH2CyJj52Ty0uVZ/LiDqUL" +
            "UfaF3LgzWUQD9t3o/TKtneSM9Gl240O8Dd+j4rRTnEJp3+oM3aBHOmEXNQJBAJR5" +
            "K/7FieXhcKU/BsCwB7kuVU6wV2OqOeR8Mpwxaz/jXt+LZM6kN9OEiBETjG9MwEto" +
            "ToHUMQq2HAe15MtVJDECQF7lh83AMlL31AtdmFkaHvqu8vrwYiDwqlam+dGADWPG" +
            "+Cpn7fcXw0wBqRLLMLymz6IAp2mJCN+N7W76j8GP08E=";


    public static String tip() {
        return tip[(int) (Math.random() * 20)];
    }

    //交通提示语
    private static String[] tip = new String[]{
            "安全靠着你我他，和谐交通靠大家！",
            "你争我抢道路窄，互谦互让心路宽！",
            "与文明一起上路，伴平安一起回家！",
            "人人需要文明交通，交通需要人人文明！",
            "用礼让传递文明，用安全构筑和谐！",
            "出行因礼让而畅通，道路因畅通而和谐！",
            "一路文明一路情，用安全构筑和谐！",
            "带十分小心上路，携一份平安回家！",
            "出行多点小心，家人少点担心！",
            "冒险是事故之根，谨慎为安全之本！",
            "文明在于一言一行，安全源于一点一滴",
            "创优良交通秩序，闪精神文明之光！",
            "交通安全系万家，平平安安是幸福！",
            "安全开车是大事，文明走路非小节！",
            "红灯常在心中亮，绿灯才能伴一生！",
            "树立安全第一的思想，落实预防为主的方针",
            "多一分麻痹，多一分危险。多一些谨慎，多一些安全！",
            "交通安全系万家，平平安安是幸福！",
            "创优良交通秩序，闪精神文明之光！",
            "文明在于一言一行，安全源于一点一滴！"
    };

    public final static String KEY_BUTTON_TOP_LEFT = "topleft";
    public final static String KEY_BUTTON_TOP_RIGHT = "topright";
    public final static String KEY_BUTTON_BOTTOM_LEFT = "bottomleft";
    public final static String KEY_BUTTON_BOTTOM_RIGHT = "bottomright";
}
