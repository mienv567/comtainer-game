package com.fanwe.live.animator;


public class SDAnimator
{
	/** 平移 */
	public static final int TYPE_TRANSLATE = 0;
	/** 缩放 */
	public static final int TYPE_SCALE = 1;
	/** 透明 */
	public static final int TYPE_ALPHA = 2;

	/** 减速 */
	public static final int INTERPOLATOR_DECELERATE = 0;
	/** 匀速 */
	public static final int INTERPOLATOR_UNIFORM = 1;
	/** 加速 */
	public static final int INTERPOLATOR_ACCELERATE = 2;

	private int type = TYPE_TRANSLATE;
	private int duration;
	private int interpolator;

	// 平移
	private float percent;

	// 缩放
	private float start_scale;
	private float end_scale;

	// 透明度
	private float start_alpha;
	private float end_alpha;

	// add
	private PositionModel startPosition;
	private PositionModel endPosition;

	public PositionModel getStartPosition()
	{
		return startPosition;
	}

	public void setStartPosition(PositionModel startPosition)
	{
		this.startPosition = startPosition;
	}

	public PositionModel getEndPosition()
	{
		return endPosition;
	}

	public void setEndPosition(PositionModel endPosition)
	{
		this.endPosition = endPosition;
	}

	public float getStart_alpha()
	{
		return start_alpha;
	}

	public void setStart_alpha(float start_alpha)
	{
		this.start_alpha = start_alpha;
	}

	public float getEnd_alpha()
	{
		return end_alpha;
	}

	public void setEnd_alpha(float end_alpha)
	{
		this.end_alpha = end_alpha;
	}

	public float getStart_scale()
	{
		return start_scale;
	}

	public void setStart_scale(float start_scale)
	{
		this.start_scale = start_scale;
	}

	public float getEnd_scale()
	{
		return end_scale;
	}

	public void setEnd_scale(float end_scale)
	{
		this.end_scale = end_scale;
	}

	public float getPercent()
	{
		return percent;
	}

	public void setPercent(float percent)
	{
		this.percent = percent;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}

	public int getInterpolator()
	{
		return interpolator;
	}

	public void setInterpolator(int interpolator)
	{
		this.interpolator = interpolator;
	}

}
