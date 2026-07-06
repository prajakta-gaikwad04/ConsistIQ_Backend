package com.may26.task.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.may26.task.dto.AchievementDTO;
import com.may26.task.dto.DashboardDTO;
import com.may26.task.dto.StreakCalendarDTO;
import com.may26.task.dto.TaskRequestDto;
import com.may26.task.dto.TaskResponseDto;
import com.may26.task.entity.Task;
import com.may26.task.enums.TaskPriority;
import com.may26.task.enums.TaskStatus;
import com.may26.task.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
	@PostMapping
	public TaskResponseDto createTask(
			@Valid @RequestBody TaskRequestDto dto ,Principal principal) {
		
		System.out.println("CREATE TASK API HIT");
		return taskService.createTask(dto,principal.getName());
	}
	
	@GetMapping 
	public List<TaskResponseDto> getMyTasks(Principal principal){
		return taskService.getMyTasks(principal.getName());
	}
	
	@GetMapping("/status/{status}")
	public List<TaskResponseDto> getTasksByStatus(
	        @PathVariable TaskStatus status,
	        Principal principal) {

	    return taskService.getTasksByStatus(
	            principal.getName(),
	            status);
	}
	
	@GetMapping("/priority/{priority}")
	public List<TaskResponseDto> getTasksByPriority(
	        @PathVariable TaskPriority priority,
	        Principal principal) {

	    return taskService.getTasksByPriority(
	            principal.getName(),
	            priority);
	}
	
	@GetMapping("/category/{category}")
	public List<TaskResponseDto> getTasksByCategory(
	        @PathVariable String category,
	        Principal principal) {

	    return taskService.getTasksByCategory(
	            principal.getName(),
	            category);
	}
	
	
	
	@GetMapping("/{id}")
	public TaskResponseDto getTaskById(@PathVariable Long id, Principal principal) {
		return taskService.getTaskById(id,principal.getName());
	}
	
	
	@PutMapping("/{id}")
	public TaskResponseDto updateTask(
	        @PathVariable Long id,
	        @Valid @RequestBody TaskRequestDto dto,
	        Principal principal) {

	    return taskService.updateTask(
	            id,
	            dto,
	            principal.getName());
	}
	
	@DeleteMapping("/{id}")
	public String deleteTask(
	        @PathVariable Long id,
	        Principal principal) {

	    return taskService.deleteTask(
	            id,
	            principal.getName());
	}
	
	@PatchMapping("/{id}/complete")
	public Task completeTask(
	        @PathVariable Long id,
	        Principal principal) {

	    return taskService.completeTask(
	            id,
	            principal.getName());
	}
	@GetMapping("/paged")
	public Page<TaskResponseDto> getTasks(

	        @RequestParam(defaultValue = "0")
	        int page,

	        @RequestParam(defaultValue = "5")
	        int size,

	        @RequestParam(defaultValue = "id")
	        String sortBy,

	        @RequestParam(defaultValue = "asc")
	        String sortOrder,

	        @RequestParam(required = false)
	        String search,

	        @RequestParam(required = false)
	        TaskStatus status,

	        @RequestParam(required = false)
	        TaskPriority priority,

	        @RequestParam(required = false)
	        String category,
	        

	        Principal principal
	) {

	    return taskService.getTasks(
	            principal.getName(),
	            page,
	            size,
	            sortBy,
	            sortOrder,
	            search,
	            status,
	            priority,
	            category
	        
	    );
	}
	
	@GetMapping("/search")
	public List <TaskResponseDto> searchTask(@RequestParam String keyword, Principal principal){
		return taskService.searchTask(principal.getName(), keyword);
	}
	
	
	@GetMapping("/dashboard")
	public ResponseEntity<DashboardDTO> getDashboard(
	        Authentication authentication) {

	    String email = authentication.getName();

	    return ResponseEntity.ok(
	            taskService.getDashboardData(email)
	    );
	}
	
	
	@GetMapping("/filter")
	public ResponseEntity<List<Task>> filterTasks(
	        @RequestParam TaskStatus status,
	        @RequestParam TaskPriority priority,
	        @RequestParam String category,
	        Authentication authentication) {

	    String email = authentication.getName();

	    return ResponseEntity.ok(
	            taskService.filterTasks(
	                    email,
	                    status,
	                    priority,
	                    category)
	    );
	}
	
	
	@GetMapping("/due-today")
	public ResponseEntity<List<Task>> getDueTodayTasks(
	        Authentication authentication) {

	    String email = authentication.getName();

	    return ResponseEntity.ok(
	            taskService.getDueTodayTasks(email)
	    );
	}
	
	
	@GetMapping("/overdue")
	public ResponseEntity<List<Task>> getOverdueTasks(
	        Authentication authentication) {

	    String email = authentication.getName();

	    return ResponseEntity.ok(
	            taskService.getOverdueTasks(email)
	    );
	}
	
	
	@PostMapping("/{id}/upload")
	public ResponseEntity<String> uploadFile(
	        @PathVariable Long id,
	        @RequestParam("file") MultipartFile file) throws IOException {

	    return ResponseEntity.ok(
	            taskService.uploadFile(id, file)
	    );
	}
	
	@GetMapping("/upcoming")
	public ResponseEntity<List<Task>> getUpcomingTasks(
	        Authentication authentication) {

	    String email = authentication.getName();

	    return ResponseEntity.ok(
	            taskService.getUpcomingTasks(email)
	    );
	}
	
	
	@GetMapping("/calendar")
	public ResponseEntity<
	        List<StreakCalendarDTO>
	> getCalendar(
	        Authentication authentication
	) {

	    return ResponseEntity.ok(
	            taskService.getCalendarData(
	                    authentication.getName()
	            )
	    );
	}
	
	@GetMapping("/achievements")
	public ResponseEntity<List<AchievementDTO>>
	getAchievements(
	        Authentication authentication) {

	    return ResponseEntity.ok(
	            taskService.getAchievements(
	                    authentication.getName()
	            )
	    );
	}

}
