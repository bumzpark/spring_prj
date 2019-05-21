package skcc.nexcore.client.applicationext.entity;

import skcc.nexcore.client.application.base.BaseVO;

public class ScheduleVO extends BaseVO {

	private static final long serialVersionUID = 4474861119640544499L;

	public String jobId;
	public String jobName;
	public String jobDesc;
	public String jobUrl;
	public String jobCronExpression;
	public String jobParams;
	public String jobEnabled;

}
