/**
 * 
 */
package com.fanwe.live.model;

import java.util.List;

import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.LiveLikeModel;

/**
 * 
 * @author Administrator
 * @date 2016-5-17 下午6:54:44
 */
public class App_propActModel extends BaseActModel
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private List<LiveGiftModel> classification;

	private List<LiveLikeModel> likePros;

	public List<LiveGiftModel> getClassification()
	{
		return classification;
	}

	public void setClassification(List<LiveGiftModel> classification)
	{
		this.classification = classification;
	}

	public List<LiveLikeModel> getLikePros() {
		return likePros;
	}

	public void setLikePros(List<LiveLikeModel> likePros) {
		this.likePros = likePros;
	}
}
