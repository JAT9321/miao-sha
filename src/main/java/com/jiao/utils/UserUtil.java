package com.jiao.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiao.entity.TUser;
import com.jiao.vo.ResBean;

import java.util.ArrayList;

/**
 * auth:@highSky
 * create:2022/8/19
 * email:zgt9321@qq.com
 **/
public class UserUtil {


    private static void createUser(int count) throws SQLException, ClassNotFoundException, IOException {
        ArrayList<TUser> users = new ArrayList<>(count);

        System.out.println("=======create user start");

        for (int i = 0; i < count; i++) {
            TUser user = new TUser();
            user.setId(13000000000L + i);
            user.setNickname("user" + i);
            user.setPassword("ed57bf428283c77a33641d441eab00ee");
            user.setSalt("111111");
            user.setHead("");
            user.setRegisterDate(new Date());
            user.setLastLoginDate(new Date());
            user.setLoginCount(1);
            users.add(user);
        }
        System.out.println("=======create user over");
        System.out.println("=======insert user start");
        Connection conn = getConnection();
        String sql = "insert into t_user(id,nickname,password,salt,register_date,last_login_date,login_count) values(?,?,?,?,?,?,?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        for (int i = 0; i < count; i++) {
            TUser user = users.get(i);
            statement.setLong(1, user.getId());
            statement.setString(2, user.getNickname());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getSalt());
            statement.setTimestamp(5, new Timestamp(user.getRegisterDate().getTime()));
            statement.setTimestamp(6, new Timestamp(user.getLastLoginDate().getTime()));
            statement.setInt(7, user.getLoginCount());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
        conn.close();
        System.out.println("=======insert user over");
        System.out.println("=======get userId and userTicker start");
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("C:\\Users\\zgt\\Desktop\\userInfo.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        file.createNewFile();
        for (int i = 0; i < count; i++) {
            TUser user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" +
                    Md5Util.inputPassToFormPass("111111");
            outputStream.write(params.getBytes());
            outputStream.flush();
            InputStream inputStream = httpURLConnection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) >= 0) {
                baos.write(buffer, 0, len);
            }
            inputStream.close();
            baos.close();
            String response = new String(baos.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            ResBean respBean = mapper.readValue(response, ResBean.class);
            String userTicket = ((String) respBean.getObject());
            System.out.println("create userTicket : " + user.getId());
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getId());
        }
        raf.close();
        System.out.println("=======over all task");
    }


    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://localhost:3306/miao_sha?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        createUser(1000);
    }
}
