package im.util;

import im.model.ErrorMessagePacket;
import im.model.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

@ChannelHandler.Sharable
public class PacketEncoderAndDecoder extends MessageToMessageCodec<ByteBuf, Packet> {

    public final static PacketEncoderAndDecoder INSTANCE = new PacketEncoderAndDecoder();

    private PacketEncoderAndDecoder(){}

    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        out.add(ProtocolUtil.encoder(msg, ctx.channel().alloc().buffer()));
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        Packet packet = ProtocolUtil.decoder(msg);
        if(packet != null) {
            out.add(packet);
        }else{
            out.add(new ErrorMessagePacket());
        }
    }

}
