package com.zkr.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zkr.control.GlobalFieldInformation;
import com.zkr.execCMD.ICmdExecMethod;


/**
 * 客户端调用的总方法
 * 
 * @author lihongchen
 * 
 */
public class clientService {

	private static Log log = LogFactory.getLog(clientService.class);

	public static void main(String[] args) {

		try {

			if (args.length != 2) {
				System.out.println(GlobalFieldInformation.cmd_args_isnull);
				log.error(GlobalFieldInformation.cmd_args_isnull);
			}

			String cmdstr = args[0];
			String id = args[1];

			ICmdExecMethod icem = (ICmdExecMethod) Class.forName(
					"com.zkr.cmd." + cmdstr + "").newInstance();
			
			
			System.out.println(icem.exec(id));

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
		}

	}

}
