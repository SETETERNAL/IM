package im.util;

import im.model.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cch
 * @date 2021/6/21 14:03
 */
@Slf4j
public class PacketEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        ProtocolUtil.encoder((Packet) msg, out);
    }
}
