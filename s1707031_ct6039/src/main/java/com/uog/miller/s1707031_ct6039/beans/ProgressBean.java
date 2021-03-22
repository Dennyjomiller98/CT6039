package com.uog.miller.s1707031_ct6039.beans;

public class ProgressBean
{
	private int fOverdueHandins;
	private int fOnTimeHandins;
	private int fNotSubmitted;
	private int fTotalRed;
	private int fTotalAmber;
	private int fTotalGreen;
	private int fTotalHomeworks;
	private String fChild;

	public ProgressBean()
	{
		//Empty No-Args
	}

	public void setOverdueHandins(int overdueHandins)
	{
		fOverdueHandins = overdueHandins;
	}
	public int getOverdueHandins()
	{
		return fOverdueHandins;
	}

	public void setOnTimeHandins(int onTimeHandins)
	{
		fOnTimeHandins = onTimeHandins;
	}
	public int getOnTimeHandins()
	{
		return fOnTimeHandins;
	}

	public void setNotSubmitted(int notSubmitted)
	{
		fNotSubmitted = notSubmitted;
	}
	public int getNotSubmitted()
	{
		return fNotSubmitted;
	}

	public void setTotalRed(int totalRed)
	{
		fTotalRed = totalRed;
	}
	public int getTotalRed()
	{
		return fTotalRed;
	}

	public void setTotalAmber(int totalAmber)
	{
		fTotalAmber = totalAmber;
	}
	public int getTotalAmber()
	{
		return fTotalAmber;
	}

	public void setTotalGreen(int totalGreen)
	{
		fTotalGreen = totalGreen;
	}
	public int getTotalGreen()
	{
		return fTotalGreen;
	}

	public void setTotalHomeworks(int i)
	{
		fTotalHomeworks = i;
	}
	public int getTotalHomeworks()
	{
		return fTotalHomeworks;
	}

	public void setChild(String childEmail)
	{
		fChild = childEmail;
	}
	public String getChild()
	{
		return fChild;
	}
}
