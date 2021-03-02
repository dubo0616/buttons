package com.gaia.button.model;


import com.gaia.button.net.BaseResult;

import java.io.Serializable;

/**
 * 权证组操作信息详情 (最外层)
 *
 * Created by zsz on 16/7/12.
 */
public class GetUploadTokenAliModel extends BaseResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String AccessKeyId = "";
    private String AccessKeySecret = "";
    private String Expiration = "";
    private String SecurityToken = "";
    private String domain_path;
//    "AccessKeyId": "STS.NJB5rrkzgA2XBZ1JAG1BHKLNV",
//            "AccessKeySecret": "3oX7pg3SkbxH93jZNbo7t73YKnp8A5U7ifCfxwbHQ5WY",
//            "Expiration": 3600,
//            "Expiration_time": "",
//            "SecurityToken": "CAIS6gF1q6Ft5B2yfSjIr4n3fsjGhqVG9vCzQHyArkESPc1kpInltDz2IH9JfnVtB+sYtP8ylG5Y6fcalqJ4T55IQ1Dza8J148ybPKZRzs+T1fau5Jko1beHewHKeTOZsebWZ+LmNqC\/Ht6md1HDkAJq3LL+bk\/Mdle5MJqP+\/UFB5ZtKWveVzddA8pMLQZPsdITMWCrVcygKRn3mGHdfiEK00he6Tgis\/TjkpPE0HeE0g2mkN1yjp\/qP52pY\/NrOJpCSNqv1IR0DPGejXQMtkYRqv0t0fwaqWiW5sv5OXB02AmPPeTu9dliPJvf59AagAEdQslsXrwZ2uZVlBIJ6Z3e6Uf71vP5dw9MxgBkjhG6Kdgs+tCr4hg\/4kQGtNTfoayOuh+Oveqzz17yC81P+j+qBLZJ45r3OvNcfzBuPzZRXpY4gsMyfcSbube31joVWzvVnrd\/Qf1XceQF5B5\/xOO1jUBhFMRrIdKLXhV0bdKxpQ=="




    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        AccessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return AccessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        AccessKeySecret = accessKeySecret;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String expiration) {
        Expiration = expiration;
    }

    public String getSecurityToken() {
        return SecurityToken;
    }

    public void setSecurityToken(String securityToken) {
        SecurityToken = securityToken;
    }

    public String getDomain_path() {
        return domain_path;
    }

    public void setDomain_path(String domain_path) {
        this.domain_path = domain_path;
    }

    @Override
    public String toString() {
        return "GetUploadTokenAliModel{" +
                "AccessKeyId='" + AccessKeyId + '\'' +
                ", AccessKeySecret='" + AccessKeySecret + '\'' +
                ", Expiration=" + Expiration +
                ", SecurityToken='" + SecurityToken + '\'' +
                ", domain_path='" + domain_path + '\'' +
                '}';
    }
}
