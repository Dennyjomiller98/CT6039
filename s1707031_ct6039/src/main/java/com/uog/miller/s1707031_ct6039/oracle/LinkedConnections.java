package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.ChildBean;
import java.util.ArrayList;

public class LinkedConnections extends AbstractOracleConnections
{
	//Allows linking of Child/Parent accounts and Class/Children, to allow Parents/Guardian to view more information
	public LinkedConnections()
	{
		//Empty Constructor
	}

	public void linkChildren(String parent, String child)
	{
		//Any children linked allow the parent/guardian to view info on the child
	}

	public ArrayList<ChildBean> getChildren()
	{
		//TODO
		ArrayList<ChildBean> bean = new ArrayList<>();
		//Any children linked are retrieved, allowing parent to view child's' progress/request feedback from teacher, etc
		return bean;
	}
}
