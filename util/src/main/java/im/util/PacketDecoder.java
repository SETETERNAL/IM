package im.util;

import im.model.ErrorMessagePacket;
import im.model.JsonPacket;
import im.model.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author cch
 * @date 2021/6/21 14:01
 */
@Slf4j
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Packet packet = ProtocolUtil.decoder(in);
        if(packet != null) {
            out.add(packet);
        }else{
            out.add(new ErrorMessagePacket());
        }
    }
}
