package com.uog.miller.s1707031_ct6039.oracle;

public class LinkedConnections extends AbstractOracleConnections
{
	//Allows linking of Child/Parent accounts, to allow Parents/Guardian to view more information
	public LinkedConnections()
	{
		//Empty Constructor
	}

	public void linkChildren(String parent, String child)
	{
		//Any children linked allow the parent/guardian to view info on the child
	}

	public void getChildren(String parent)
	{
		//Any children linked are retrieved, allowing parent to view child's' progress/request feedback from teacher, etc
	}
}
