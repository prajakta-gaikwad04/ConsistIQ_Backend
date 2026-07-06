package com.may26.task.dto;

public class DashboardDTO {
	
	private long totalTasks;
	private long planned;
	private long inProgressTasks;
	private long completedTask;
	private double completionPercentage;
	
	private long todayCompletedTasks;
	private long dailyGoal;
	private int productivityScore;
	public DashboardDTO() {
		super();
	}

	
	


	public DashboardDTO(long totalTasks, long planned, long inProgressTasks, long completedTask,
			double completionPercentage, long todayCompletedTasks, long dailyGoal, int productivityScore) {
		super();
		this.totalTasks = totalTasks;
		this.planned = planned;
		this.inProgressTasks = inProgressTasks;
		this.completedTask = completedTask;
		this.completionPercentage = completionPercentage;
		this.todayCompletedTasks = todayCompletedTasks;
		this.dailyGoal = dailyGoal;
		this.productivityScore = productivityScore;
	}





	public long getTotalTasks() {
		return totalTasks;
	}
	public void setTotalTasks(long totalTasks) {
		this.totalTasks = totalTasks;
	}

	public long getPlanned() {
		return planned;
	}
	public void setPlanned(long planned) {
		this.planned = planned;
	}

	public long getInProgressTasks() {
		return inProgressTasks;
	}
	public void setInProgressTasks(long inProgressTasks) {
		this.inProgressTasks = inProgressTasks;
	}
	public long getCompletedTask() {
		return completedTask;
	}
	public void setCompletedTask(long completedTask) {
		this.completedTask = completedTask;
	}
	public double getCompletionPercentage() {
		return completionPercentage;
	}
	public void setCompletionPercentage(double completionPercentage) {
		this.completionPercentage = completionPercentage;
	}

	public long getTodayCompletedTasks() {
		return todayCompletedTasks;
	}

	public void setTodayCompletedTasks(long todayCompletedTasks) {
		this.todayCompletedTasks = todayCompletedTasks;
	}

	public long getDailyGoal() {
		return dailyGoal;
	}

	public void setDailyGoal(long dailyGoal) {
		this.dailyGoal = dailyGoal;
	}

	public void setPendingTasks(long pendingTasks) {
		this.planned = pendingTasks;
	}


	public int getProductivityScore() {
		return productivityScore;
	}


	public void setProductivityScore(int productivityScore) {
		this.productivityScore = productivityScore;
	}
	
	
	
	

}
