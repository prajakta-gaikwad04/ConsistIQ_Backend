package com.may26.task.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.may26.entity.User;
import com.may26.task.entity.Task;
import com.may26.task.enums.TaskPriority;
import com.may26.task.enums.TaskStatus;

public class TaskSpecification {

    public static Specification<Task> filterTasks(
            User user,
            String search,
            TaskStatus status,
            TaskPriority priority,
            String category
    ) {

        return (root, query, cb) -> {

            var predicate = cb.equal(root.get("user"), user);

            if (search != null && !search.isBlank()) {
                predicate = cb.and(
                        predicate,
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + search.toLowerCase() + "%"
                        )
                );
            }

            if (status != null) {
                predicate = cb.and(
                        predicate,
                        cb.equal(root.get("status"), status)
                );
            }

            if (priority != null) {
                predicate = cb.and(
                        predicate,
                        cb.equal(root.get("priority"), priority)
                );
            }

            if (category != null && !category.isBlank()) {
                predicate = cb.and(
                        predicate,
                        cb.equal(root.get("category"), category)
                );
            }

            return predicate;
        };
    }
}