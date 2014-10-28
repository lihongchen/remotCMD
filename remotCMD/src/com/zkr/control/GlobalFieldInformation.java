package com.zkr.control;
/**
 * 记录所有异常信息
 * @author lihongchen
 * 
 */
public class GlobalFieldInformation {
	
	
	// error 信息
	public static String sqlarea_error = "要执行任务中的 sqlarea 不服和要求";
	public static String machine_does_not_exist = "配置执行sql 语句 机器的信息不存在";
	public static String db_not_in_ini="数据库链接信息没有在init配置文件中找到";
	public static String db_info_in_init_error ="在配置文件中的数据库配置信息错误";
    public static String sql_info_error="要执行的sql有问题";
    public static String sql_dmlorddl_error="dmlorddl 字段出错";
    public static String sql_cmd_is_null="要执行的sql语句为空";
    public static String insert_into_db_error="向数据库中插入数据失败";
    public static String update_status_error="更新任务表状态出错";
    public static String cmd_conn_server_error="链接远程机器失败";
	public static String cmd_rem_ip_isnull="执行cmd命令的远程机器ip为空";
	public static String cmd_rem_id_notin_db="执行远程命令机器不存在（没有在数据库中）";
	public static String cmd_args_isnull="cmd 执行命令时没有参数";
	public static String cmd_init_no_remip="没有配置本地ip地址";
    
    
    //状态信息
    public static String exec_ok = "执行命令成功";
    
    
    
    //固定字段信息
    public static String status_succ="1001";//任务执行成功
    public static String status_fail="1002";//任务执行失败
    
  //执行远程方法的名称  
  public static class cmdMethodName{
	  public static String Init="Init";
	  public static String Runs="Runs";
  }
}
