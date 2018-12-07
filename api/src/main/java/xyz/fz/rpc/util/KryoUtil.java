package xyz.fz.rpc.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import io.netty.buffer.ByteBuf;
import xyz.fz.rpc.exception.NotEnoughException;
import xyz.fz.rpc.model.Request;
import xyz.fz.rpc.model.Response;

import java.io.ByteArrayOutputStream;

public class KryoUtil {
    private static final ThreadLocal<Kryo> kryoHolder = ThreadLocal.withInitial(KryoUtil::createKryo);

    private static Kryo createKryo() {
        Kryo kryo = new Kryo();
        kryo.register(Request.class);
        kryo.register(Response.class);
        return kryo;
    }

    private static Kryo getKryo() {
        return kryoHolder.get();
    }

    public static <T> T decodeByteBuf(ByteBuf in, Class<T> clazz) throws Exception {
        if (in.readableBytes() < 4) {
            throw new NotEnoughException();
        }

        in.markReaderIndex();

        int objectSize = in.readInt();

        if (in.readableBytes() < objectSize) {
            in.resetReaderIndex();
            throw new NotEnoughException();
        }

        byte[] objectBytes = new byte[objectSize];
        in.readBytes(objectBytes);
        Input input = new Input(objectBytes);
        return KryoUtil.getKryo().readObject(input, clazz);
    }

    public static void encodeByteBuf(Object msg, ByteBuf out) throws Exception {
        Output output = new Output(new ByteArrayOutputStream(8 * 1024), 8 * 1024);
        KryoUtil.getKryo().writeObject(output, msg);
        out.writeInt(output.getBuffer().length);
        out.writeBytes(output.getBuffer());
    }
}
