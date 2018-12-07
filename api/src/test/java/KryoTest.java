import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import xyz.fz.rpc.model.Request;
import xyz.fz.rpc.model.Response;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class KryoTest {

    public static void main(String[] args) throws FileNotFoundException {

        Request request = new Request();
        request.setId(System.currentTimeMillis());
        request.setClazz("xyz.fz.rpc.service.RpcService");
        request.setMethod("sayHello");
        request.setArgs(new Object[]{"fz"});
        kryoRequestTest(request);

        Response response = new Response();
        response.setId(System.currentTimeMillis());
        response.setSuccess(true);
        response.setData("Hello, fz");
        response.setErrorMessage("");
        kryoResponseTest(response);
    }

    private static void kryoRequestTest(Request writeRequest) throws FileNotFoundException {

        Kryo kryo = new Kryo();
        kryo.register(Request.class);

        Output output = new Output(new FileOutputStream("request.bin"));
        System.out.println("write request: " + writeRequest);
        kryo.writeObject(output, writeRequest);
        output.close();

        Input input = new Input(new FileInputStream("request.bin"));
        Request readRequest = kryo.readObject(input, Request.class);
        System.out.println("read request: " + readRequest);
        input.close();
    }

    private static void kryoResponseTest(Response writeResponse) throws FileNotFoundException {

        Kryo kryo = new Kryo();
        kryo.register(Response.class);

        Output output = new Output(new FileOutputStream("response.bin"));
        System.out.println("write response: " + writeResponse);
        kryo.writeObject(output, writeResponse);
        output.close();

        Input input = new Input(new FileInputStream("response.bin"));
        Response readResponse = kryo.readObject(input, Response.class);
        System.out.println("read response: " + readResponse);
        input.close();
    }
}
