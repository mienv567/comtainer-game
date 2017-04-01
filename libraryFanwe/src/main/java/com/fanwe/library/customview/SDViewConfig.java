package com.fanwe.library.customview;

import com.fanwe.library.SDLibrary;
import com.fanwe.library.config.SDLibraryConfig;

public abstract class SDViewConfig implements Cloneable
{
	public SDLibraryConfig mLibraryConfig = SDLibrary.getInstance().getConfig();

	public SDViewConfig()
	{
		setDefaultConfig();
	}

	public abstract void setDefaultConfig();
	
	@Override
	public SDViewConfig clone()
	{
		try
		{
			return (SDViewConfig) super.clone();
		} catch (Exception e)
		{
			return null;
		}
	}
}
