package com.task.util;

import com.task.dto.TaskDTO;
import com.task.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface TaskMapper {

    @Mappings({
            @Mapping(target = "taskId", source = "id"),
            @Mapping(target = "projectName", source = "project.name")
    })
    TaskDTO taskToTaskDTO(Task task);

    Task taskDTOToTask(TaskDTO taskDTO);
}