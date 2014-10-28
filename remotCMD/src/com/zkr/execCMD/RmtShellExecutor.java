package com.zkr.execCMD;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.zkr.control.GlobalFieldInformation;

public class RmtShellExecutor { 
   
    /** *//**  */
    private Connection conn;
    /** *//** 远程机器IP */
    private String     ip;
    /** *//** 用户名 */
    private String     usr;
    /** *//** 密码 */
    private String     psword;
    private String     charset = Charset.defaultCharset().toString();

    private static final int TIME_OUT = 1000 * 5 * 60;


    /** *//**
     * 构造函数
     * @param ip
     * @param usr
     * @param ps
     */
    public RmtShellExecutor(String ip, String usr, String ps) {
        this.ip = ip;
        this.usr = usr;
        this.psword = ps;
    }

    /** *//**
     * 登录
     *
     * @return
     * @throws IOException
     */
    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(usr, psword);
    }

    /** *//**
     * 执行脚本
     *
     * @param cmds
     * @return
     * @throws Exception
     */
    public String exec(String cmds) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        String ret = null;
        try {
            if (login()) {
                // Open a new {@link Session} on this connection
                Session session = conn.openSession();
                // Execute a command on the remote machine.
                session.execCommand(cmds);
                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, "gb2312");
               System.out.println(" bian ma  ge shi  wei  " + charset );
                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);
               
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
               
                System.out.println("outStr=" + outStr);
                System.out.println("outErr=" + outErr);
               
                session.getExitStatus();
                
                ret = outStr.trim()+"\n"+outErr.trim();
                

                
                ret = String.valueOf(ret);
        		
                System.out.println(ret);
                
            } else {
                throw new RuntimeException(GlobalFieldInformation.cmd_conn_server_error + ip); // 自定义异常类 实现略
                
            }
        }catch (Exception e) {
        
            throw new RuntimeException(e.toString() + ip); // 自定义异常类 实现略
        	
        	
        }finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    /** *//**
     * @param in
     * @param charset
     * @return
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        int numchar = 0;
        while ((numchar = in.read(buf)) != -1) {
            sb.append(new String(buf,0,numchar, charset));
        }
        return sb.toString();
    }

    public static void main(String args[]) throws Exception {
        RmtShellExecutor exe = new RmtShellExecutor("192.168.1.195", "administrator", "Yzd123");
//       System.out.println(exe.login());
        // 执行myTest.sh 参数为java Know dummy
//        System.out.println(exe.exec("sh /webapp/myshell/myTest.sh java Know dummy"));
        System.out.println(exe.exec(new String("f:/run.bat 很好")));
        
        
//        System.out.println(exe.exec("cmd.exe /c cd f: &&  f: &&  run.bat hah"));
//        System.out.println(exe.exec("cmd.exe /c cd  f:"));
//        System.out.println(exe.exec("echo "));
        
        
        
//        exe.exec("uname -a && date && uptime && who");
    }
} 