package com.xianyuli.bio.rpc.provider;

import com.xianyuli.bio.rpc.api.service.IUserService;
import com.xianyuli.bio.rpc.provider.impl.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

@SpringBootApplication
public class RpcProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcProviderApplication.class, args);
        try {
            provider();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void provider() throws IOException {
        ServerSocket socket = null;
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        try {
            socket = new ServerSocket(8888);
            while (true) {
                Socket accept = socket.accept();
                inputStream = new ObjectInputStream(accept.getInputStream());
                String apiClassName = inputStream.readUTF();
                String methodName = inputStream.readUTF();
                Class[] paramTypes = (Class[]) inputStream.readObject();
                Object[] args4Method = (Object[]) inputStream.readObject();
                Class clazz = null;
                //2.服务注册，找到具体的实现类
                if (apiClassName.equals(IUserService.class.getName())){
                    clazz = UserServiceImpl.class;
                }
                //3.执行UserServiceImpl的方法
                Method method = clazz.getMethod(methodName,paramTypes);
                Object invoke = method.invoke(clazz.newInstance(),args4Method);

                //4.返回结果给客户端
                outputStream = new ObjectOutputStream(accept.getOutputStream());
                outputStream.writeObject(invoke);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //5.关闭连接
            if (outputStream!=null) {
                outputStream.close();
            }
            if (inputStream!=null) {
                inputStream.close();
            }
            if (socket!=null) {
                socket.close();
            }
        }


    }

}
