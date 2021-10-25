package im.model;

import io.netty.util.AttributeKey;

public class Attributes {

    public final static AttributeKey<Boolean> HAS_LOGIN = AttributeKey.newInstance("HAS_LOGIN");
    public final static AttributeKey<Member> MEMBER_INFO = AttributeKey.newInstance("MEMBER_INFO");
}
