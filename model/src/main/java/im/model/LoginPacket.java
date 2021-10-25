package im.model;


import im.model.enums.CommandEnum;

import java.util.StringJoiner;

/**
 * @author cch
 * @date 2021/6/17 16:42
 */
public class LoginPacket extends Packet{

    private String loginName;
    private String password;

    public LoginPacket() {
    }

    public LoginPacket(String loginName, String password) {
        this.loginName = loginName;
        this.password = password;
    }

    @Override
    public byte getCommand() {
        return CommandEnum.LOGIN.getCode();
    }
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LoginPacket.class.getSimpleName() + "[", "]")
                .add("loginName='" + loginName + "'")
                .add("password='" + password + "'")
                .add("version=" + version)
                .toString();
    }
}
