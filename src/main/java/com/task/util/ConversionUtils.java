package com.task.util;

import com.task.dto.TaskDTO;
import com.task.entity.Task;
import org.mapstruct.factory.Mappers;

public class ConversionUtils {

    private static final TaskMapper TASK_MAPPER = Mappers.getMapper(TaskMapper.class);

    public static TaskDTO convertToTaskDTO(Task task) {
        return TASK_MAPPER.taskToTaskDTO(task);
    }

    public static Task convertToTask(TaskDTO taskDTO) {
        return TASK_MAPPER.taskDTOToTask(taskDTO);
    }
}
