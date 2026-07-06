package com.may26.task.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.may26.entity.User;
import com.may26.exception.TaskNotFoundException;
import com.may26.exception.UnauthorizedTaskAccessException;
import com.may26.exception.UserNotFoundException;
import com.may26.repository.UserRepository;
import com.may26.task.dto.AchievementDTO;
import com.may26.task.dto.DashboardDTO;
import com.may26.task.dto.StreakCalendarDTO;
import com.may26.task.dto.TaskRequestDto;
import com.may26.task.dto.TaskResponseDto;
import com.may26.task.entity.Task;
import com.may26.task.enums.TaskPriority;
import com.may26.task.enums.TaskStatus;
import com.may26.task.repository.TaskRepository;
import com.may26.task.specifications.TaskSpecification;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public TaskResponseDto createTask(TaskRequestDto dto ,String email) {
		
		User user=userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
		Task task=new Task();
		
		task.setTitle(dto.getTitle());
		task.setDescription(dto.getDescription());
	    task.setStatus(dto.getStatus());
		task.setPriority(dto.getPriority());
		task.setDueDate(dto.getDueDate());
		task.setCategory(dto.getCategory());
		
		task.setUser(user);
		
		Task savedTask=taskRepository.save(task);
		
		TaskResponseDto response=new TaskResponseDto();
		
		response.setId(savedTask.getId());
		response.setTitle(savedTask.getTitle());
		response.setDescription(savedTask.getDescription());
		response.setStatus(savedTask.getStatus());
		response.setPriority(savedTask.getPriority());
		response.setDueDate(savedTask.getDueDate());
		response.setCategory(savedTask.getCategory());

	   return response;
	}
	
	public List<TaskResponseDto> getMyTasks(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));

	    return taskRepository.findByUser(user)
	            .stream()
	            .map(task -> {

	                TaskResponseDto dto =
	                        new TaskResponseDto();

	                dto.setId(task.getId());
	                dto.setTitle(task.getTitle());
	                dto.setDescription(task.getDescription());
	                dto.setStatus(task.getStatus());
	                dto.setPriority(task.getPriority());
	                dto.setDueDate(task.getDueDate());
	                dto.setCategory(task.getCategory());

	                return dto;

	            }).collect(Collectors.toList());
	}
	
	public List<TaskResponseDto> getTasksByStatus(
	        String email,
	        TaskStatus status) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));

	    return taskRepository.findByUserAndStatus(user, status)
	            .stream()
	            .map(task -> {

	                TaskResponseDto dto =
	                        new TaskResponseDto();

	                dto.setId(task.getId());
	                dto.setTitle(task.getTitle());
	                dto.setDescription(task.getDescription());
	                dto.setStatus(task.getStatus());
	                dto.setPriority(task.getPriority());
	                dto.setDueDate(task.getDueDate());
	                dto.setCategory(task.getCategory());

	                return dto;

	            }).collect(Collectors.toList());
	}
	
	public List<TaskResponseDto> getTasksByPriority(
	        String email,
	        TaskPriority priority) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));

	    return taskRepository.findByUserAndPriority(user, priority)
	            .stream()
	            .map(task -> {

	                TaskResponseDto dto = new TaskResponseDto();

	                dto.setId(task.getId());
	                dto.setTitle(task.getTitle());
	                dto.setDescription(task.getDescription());
	                dto.setStatus(task.getStatus());
	                dto.setPriority(task.getPriority());
	                dto.setDueDate(task.getDueDate());
	                dto.setCategory(task.getCategory());

	                return dto;
	            })
	            .collect(Collectors.toList());
	}
	
	
	public List<TaskResponseDto> getTasksByCategory(
	        String email,
	        String category) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));

	    return taskRepository.findByUserAndCategory(user, category)
	            .stream()
	            .map(task -> {

	                TaskResponseDto dto = new TaskResponseDto();

	                dto.setId(task.getId());
	                dto.setTitle(task.getTitle());
	                dto.setDescription(task.getDescription());
	                dto.setStatus(task.getStatus());
	                dto.setPriority(task.getPriority());
	                dto.setDueDate(task.getDueDate());
	                dto.setCategory(task.getCategory());

	                return dto;
	            })
	            .collect(Collectors.toList());
	}
	
	
	
	public TaskResponseDto getTaskById(Long id, String email) {

	    Task task = getTaskForCurrentUser(id, email);

	    TaskResponseDto response = new TaskResponseDto();

	    response.setId(task.getId());
	    response.setTitle(task.getTitle());
	    response.setDescription(task.getDescription());
	    response.setStatus(task.getStatus());
	    response.setPriority(task.getPriority());
	    response.setDueDate(task.getDueDate());
	    response.setCategory(task.getCategory());

	    return response;
	}
	
	
	public TaskResponseDto updateTask(
	        Long id,
	        TaskRequestDto dto,
	        String email) {

	    Task task = getTaskForCurrentUser(id, email);

	    task.setTitle(dto.getTitle());
	    task.setDescription(dto.getDescription());
	    task.setStatus(dto.getStatus());
	    task.setPriority(dto.getPriority());
	    task.setDueDate(dto.getDueDate());
	    task.setCategory(dto.getCategory());

	    Task savedTask = taskRepository.save(task);

	    TaskResponseDto response = new TaskResponseDto();

	    response.setId(savedTask.getId());
	    response.setTitle(savedTask.getTitle());
	    response.setDescription(savedTask.getDescription());
	    response.setStatus(savedTask.getStatus());
	    response.setPriority(savedTask.getPriority());
	    response.setDueDate(savedTask.getDueDate());
	    response.setCategory(savedTask.getCategory());

	    return response;
	}
	
	public String deleteTask(Long id, String email) {

	    Task task = getTaskForCurrentUser(id, email);

	    taskRepository.delete(task);

	    return "Task Deleted Successfully";
	}
	
	
	
	public Task completeTask(Long id, String email) {

	    Task task = getTaskForCurrentUser(id, email);

	    task.setStatus(TaskStatus.COMPLETED);
	    
	    task.setCompletedAt(LocalDate.now());

	    return taskRepository.save(task);
	}
	
	
	public Page<TaskResponseDto> getTasks(
	        String email,
	        int page,
	        int size,
	        String sortBy,
	        String sortOrder,
	        String search,
	        TaskStatus status,
	        TaskPriority priority,
	        String category
	) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new UserNotFoundException("User not found"));

	    if(sortBy == null || sortBy.isBlank()) {
	        sortBy = "id";
	    }

	    Sort sort = sortOrder.equalsIgnoreCase("desc")
	            ? Sort.by(sortBy).descending()
	            : Sort.by(sortBy).ascending();

	    Pageable pageable = PageRequest.of(page, size, sort);

	    Page<Task> tasks = taskRepository.findAll(
	            TaskSpecification.filterTasks(
	                    user,
	                    search,
	                    status,
	                    priority,
	                    category
	            ),
	            pageable
	    );

	    return tasks.map(this::mapToDto);
	
	}
	
	
	private TaskResponseDto mapToDto(Task task) {

	    TaskResponseDto dto = new TaskResponseDto();

	    dto.setId(task.getId());
	    dto.setTitle(task.getTitle());
	    dto.setDescription(task.getDescription());
	    dto.setStatus(task.getStatus());
	    dto.setPriority(task.getPriority());
	    dto.setDueDate(task.getDueDate());
	    dto.setCategory(task.getCategory());

	    return dto;
	}
	
	
	public List<TaskResponseDto> searchTask(String email,String keyword)
	{
		User user=userRepository.findByEmail(email).orElseThrow(() -> 
		new UserNotFoundException("User not found"));
		
		return taskRepository.findByUserAndTitleContainingIgnoreCase(user, keyword).stream().map(task -> {
			
			TaskResponseDto dto=new TaskResponseDto();
			
			 dto.setId(task.getId());
			 dto.setTitle(task.getTitle());
             dto.setDescription(task.getDescription());
             dto.setStatus(task.getStatus());
             dto.setPriority(task.getPriority());
             dto.setDueDate(task.getDueDate());
             dto.setCategory(task.getCategory());

             return dto;
		}).toList();
	}
	
	
	private Task getTaskForCurrentUser(Long id,String email) {
		
		Task task=taskRepository.findById(id)
				.orElseThrow(() ->
				new TaskNotFoundException("Task not found with id: "+id));
		
		if(!task.getUser().getEmail().equals(email)) {
			throw new UnauthorizedTaskAccessException( "You are not allowed to access this task");
		}
		return task;
	}
	
	
	
	public DashboardDTO getDashboardData(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    long total = taskRepository.countByUser(user);

	    long planned =
	            taskRepository.countByUserAndStatus(
	                    user,
	                    TaskStatus.PLANNED);

	    long inProgress =
	            taskRepository.countByUserAndStatus(
	                    user,
	                    TaskStatus.IN_PROGRESS);
	    long completed = taskRepository.countByUserAndStatus(
	            user, TaskStatus.COMPLETED);
	    long todayCompletedTasks =
	            taskRepository.countByUserAndCompletedAt(
	                    user,
	                    LocalDate.now());

	    long dailyGoal = 5;
	    double completionPercentage=0;
	    
	    if(total>0) {
	    	completionPercentage=((double) completed/total)*100;
	    }

	    int productivityScore = 0;

	 productivityScore +=
	         (int)(completionPercentage * 0.5);

	 if(dailyGoal > 0){

	     productivityScore +=
	             (int)Math.min(
	                     30,
	                     (todayCompletedTasks * 30.0)
	                     / dailyGoal
	             );
	 }

	 long overdue =
	         taskRepository
	         .findByUserAndDueDateBefore(
	                 user,
	                 LocalDate.now()
	         )
	         .stream()
	         .filter(t ->
	         t.getStatus() != TaskStatus.COMPLETED &&
	         t.getStatus() != TaskStatus.CANCELLED)
	         .count();

	 productivityScore -=
	         Math.min(
	                 20,
	                 (int)(overdue * 5)
	         );

	 if(productivityScore < 0)
	     productivityScore = 0;

	 if(productivityScore > 100)
	     productivityScore = 100;
	 return new DashboardDTO(
		        total,
		        planned,
		        inProgress,
		        completed,
		        completionPercentage,
		        todayCompletedTasks,
		        dailyGoal,
		        productivityScore
		);
	}
	
	
	public List<Task> filterTasks(
	        String email,
	        TaskStatus status,
	        TaskPriority priority,
	        String category) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    return taskRepository.findByUserAndStatusAndPriorityAndCategory(
	            user,
	            status,
	            priority,
	            category);
	}
	
	
	public List<Task> getDueTodayTasks(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    return taskRepository.findByUserAndDueDate(
	            user,
	            LocalDate.now()
	    );
	}
	
	
	public List<Task> getOverdueTasks(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    List<Task> tasks = taskRepository.findByUserAndDueDateBefore(
	            user,
	            LocalDate.now()
	    );

	    return tasks.stream()
	            .filter(task ->
	                task.getStatus() != TaskStatus.COMPLETED &&
	                task.getStatus() != TaskStatus.CANCELLED)
	            .toList();
	}
	
	
	
	
	
	public String uploadFile(Long taskId, MultipartFile file) throws IOException {

	    String uploadDir = System.getProperty("user.dir") + "/uploads/";

	    File directory = new File(uploadDir);
	    if (!directory.exists()) {
	        directory.mkdirs();
	    }

	    String fileName = System.currentTimeMillis()
	            + "_" + file.getOriginalFilename();

	    String filePath = uploadDir + fileName;

	    file.transferTo(new File(filePath));

	    Task task = taskRepository.findById(taskId)
	            .orElseThrow(() -> new RuntimeException("Task not found"));

	    task.setAttachmentPath(filePath);

	    taskRepository.save(task);

	    return "File uploaded successfully";
	}
	
	public double getCompletionRate(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));

	    long total = taskRepository.countByUser(user);
	    long completed = taskRepository.countByUserAndStatus(user, TaskStatus.COMPLETED);

	    return total == 0 ? 0 : (completed * 100.0 / total);
	}
	
	
	
	public List<LocalDate> getCompletionDates(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));

	    return taskRepository.findByUser(user)
	            .stream()
	            .filter(t -> t.getStatus() == TaskStatus.COMPLETED)
	            .map(Task::getCompletedAt)
	            .filter(date -> date != null)
	            .toList();
	}
	
	
	
	public List<Task> getUpcomingTasks(String email) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException("User not found"));

	    LocalDate today = LocalDate.now();

	    LocalDate next7Days = today.plusDays(7);

	    return taskRepository.findByUserAndDueDateBetween(
	            user,
	            today,
	            next7Days
	    ).stream()
	     .filter(task ->
	             task.getStatus() != TaskStatus.COMPLETED &&
	             task.getStatus() != TaskStatus.CANCELLED)
	     .toList();
	}
	
	
	
	
	public List<StreakCalendarDTO> getCalendarData(
	        String email
	) {

	    User user = userRepository
	            .findByEmail(email)
	            .orElseThrow(() ->
	                    new RuntimeException(
	                            "User not found"
	                    ));

	    List<StreakCalendarDTO> result =
	            new ArrayList<>();

	    LocalDate today = LocalDate.now();

	    for (int i = 29; i >= 0; i--) {

	        LocalDate date =
	                today.minusDays(i);

	        long completed =
	                taskRepository
	                .countByUserAndCompletedAt(
	                        user,
	                        date
	                );

	        result.add(
	                new StreakCalendarDTO(
	                        date,
	                        completed > 0
	                )
	        );
	    }

	    return result;
	}
	
	
	public List<AchievementDTO> getAchievements(
	        String email) {

	    User user = userRepository
	            .findByEmail(email)
	            .orElseThrow();

	    long completed =
	            taskRepository.countByUserAndStatus(
	                    user,
	                    TaskStatus.COMPLETED);

	    List<AchievementDTO> achievements =
	            new ArrayList<>();

	    achievements.add(
	            new AchievementDTO(
	                    "First Task",
	                    "🎉",
	                    completed >= 1));

	    achievements.add(
	            new AchievementDTO(
	                    "10 Tasks Completed",
	                    "🏅",
	                    completed >= 10));

	    achievements.add(
	            new AchievementDTO(
	                    "50 Tasks Completed",
	                    "🔥",
	                    completed >= 50));

	    achievements.add(
	            new AchievementDTO(
	                    "100 Tasks Completed",
	                    "👑",
	                    completed >= 100));

	    return achievements;
	}
	
	public List<TaskResponseDto> getAllTasksForAdmin() {

	    return taskRepository.findAll()
	            .stream()
	            .map(this::mapToDto)
	            .toList();
	}
	
	public String deleteTaskByAdmin(Long id) {

	    Task task = taskRepository.findById(id)
	            .orElseThrow(() ->
	                    new TaskNotFoundException("Task not found"));

	    taskRepository.delete(task);

	    return "Task deleted successfully";
	}

}

