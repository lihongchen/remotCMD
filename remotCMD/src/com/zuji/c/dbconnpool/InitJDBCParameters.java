package com.zuji.c.dbconnpool;

import java.io.InputStream;
import java.util.Properties;

public abstract class InitJDBCParameters {

	private static Properties properties = null;

	private static final String FILE_PATH = "jdbcinit.ini";

	private static void InitJDBCPara() {
		properties = new Properties();


		try {
			InputStream is = InitJDBCParameters.class.getClassLoader().getResourceAsStream(FILE_PATH);
			properties.load(is);
			is.close();
		} catch (Exception e) {
			properties = null;
			e.printStackTrace();
		}
	}

	public static Properties getProperties() {
		if (properties == null) {
			InitJDBCPara();
			return properties;
		}

		else
			return properties;
	}

	public static void main(String[] args) {
		Properties i = InitJDBCParameters.getProperties();
		// System.out.println(i);
		// System.out.println(i.get("defaultReadOnly"));
		// System.out.println(i.get("sssss"));
		boolean v;
		System.out.println(Boolean.class.getName());
	}

}
