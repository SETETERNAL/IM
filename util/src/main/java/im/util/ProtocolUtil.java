package im.util;

import im.model.JsonSerializer;
import im.model.Packet;
import im.model.Serializer;
import im.model.enums.CommandEnum;
import im.model.enums.MoShu;
import im.model.enums.SerializerEnum;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * @author cch
 * @date 2021/6/17 16:53
 */
public class ProtocolUtil {

    public static ByteBuf encoder(Packet packet, ByteBuf buffer){
        Serializer serializer = new JsonSerializer();
        byte[] data = serializer.serialize(packet);
        if(buffer == null){
            ByteBufAllocator byteBufAllocator = new PooledByteBufAllocator();
            buffer = byteBufAllocator.directBuffer(data.length + 11);
        }
        buffer.writeInt(MoShu.MAGIC_NUMBER) // 4
                .writeByte(packet.version) // 1
                .writeByte(serializer.getSerializerAlgorithm()) // 1
                .writeByte(packet.getCommand()) // 1
                .writeInt(data.length) // 4
                .writeBytes(data); // Integer.MAX
        return buffer;
    }

    public static ByteBuf encoder(Packet packet){
        return encoder(packet, null);
    }

    public static Packet decoder(ByteBuf byteBuf){
        // magic number
        int magicNumber = byteBuf.readInt();
        if(magicNumber != MoShu.MAGIC_NUMBER){
            return null;
        }
        // 跳过版本号
        byteBuf.skipBytes(1);
        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 数据包长度
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Serializer serializer = SerializerEnum.get(serializeAlgorithm).getSerializer();
        return serializer.deserialize(CommandEnum.get(command).getPacket(), bytes);
    }
}
