package com.zkr.control;
/**
 * 任务信息对应的数据表
 * @author lihongchen
 */
import java.util.Date;
 
public class TaskBean {

	private String id;
	private String username;
	private Date createdate;
	private Date execdate;
	private String exectype;
	private String rem_mip;
	private String sqlarea;
	private String dmlorddl;
	private String cmdorsql;
	private String status;
	public String getDmlorddl() {
		return dmlorddl;
	}
	public void setDmlorddl(String dmlorddl) {
		this.dmlorddl = dmlorddl;
	}
	private String remark;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getExecdate() {
		return execdate;
	}
	public void setExecdate(Date execdate) {
		this.execdate = execdate;
	}
	public String getExectype() {
		return exectype;
	}
	public void setExectype(String exectype) {
		this.exectype = exectype;
	}
	public String getRem_mip() {
		return rem_mip;
	}
	public void setRem_mip(String rem_mip) {
		this.rem_mip = rem_mip;
	}
	public String getCmdorsql() {
		return cmdorsql;
	}
	public void setCmdorsql(String cmdorsql) {
		this.cmdorsql = cmdorsql;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSqlarea() {
		return sqlarea;
	}
	public void setSqlarea(String sqlarea) {
		this.sqlarea = sqlarea;
	}
	
}
