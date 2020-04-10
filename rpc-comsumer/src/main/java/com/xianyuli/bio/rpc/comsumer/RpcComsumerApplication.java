package com.xianyuli.bio.rpc.comsumer;

import com.xianyuli.bio.rpc.api.entity.User;
import com.xianyuli.bio.rpc.api.service.IUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

@SpringBootApplication
public class RpcComsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcComsumerApplication.class, args);
        IUserService userService = (IUserService)rpc(IUserService.class);
        User user = userService.findById(123L);
        System.out.println(user);
    }

    public static Object rpc(Class clazz){
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //1.建立远程连接
                        Socket socket = new Socket("127.0.0.1",8888);
                        //2.要调用的类、方法、参数
                        String apiName = clazz.getName();
                        String methodName = method.getName();
                        //为了鉴别方法的重载，这里需要传入参数类型
                        Class[] paramTypes = method.getParameterTypes();
                        //3.传输类信息，请求远程执行结果
                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        outputStream.writeUTF(apiName);
                        outputStream.writeUTF(methodName);
                        outputStream.writeObject(paramTypes);
                        outputStream.writeObject(args);
                        outputStream.flush();

                        //4.接收返回的结果
                        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                        Object object = inputStream.readObject();
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                        return object;
                    }
                }
        );
    }
}
